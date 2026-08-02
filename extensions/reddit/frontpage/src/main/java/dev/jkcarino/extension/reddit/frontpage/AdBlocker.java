package dev.jkcarino.extension.reddit.frontpage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Set;

import okhttp3.Request;

public final class AdBlocker {

    private interface AdFilter {
        boolean test(JSONObject item) throws JSONException;
    }

    private static final String APOLLO_OPERATION_HEADER = "X-APOLLO-OPERATION-NAME";
    private static final String PDP_COMMENTS_ADS_OP = "PdpCommentsAds";

    private static final String DATA = "data";
    private static final String ELEMENTS = "elements";
    private static final String EDGES = "edges";
    private static final String NODE = "node";
    private static final String AD_PAYLOAD = "adPayload";
    private static final String GROUP_RECOMMENDATION_CONTEXT = "groupRecommendationContext";
    private static final String GROUP_RECOMMENDATION_TYPE_ID = "typeIdentifier";
    private static final String CELL_TYPE_NAME = "__typename";
    private static final String CELL_RECOMMENDATION_CONTEXT_CALL = "RichtextRecommendationContextCell";

    private static final String TYPE_ID_GAMES = "dev_platform";

    private static final String SEARCH_RECOMMENDATION = "recommendation";
    private static final String SEARCH_TRENDING_QUERIES = "trendingQueries";
    private static final String SEARCH_IS_PROMOTED = "isPromoted";

    private static final Set<String> feeds = Set.of(
            "homeV3",
            "popularV3",
            "allV3",
            "customFeedV3",
            "subredditV3"
    );

    private static final Set<String> nodeCells = Set.of("cells", "crosspostCells");

    private static final Set<String> adCells = Set.of(
            "AdBrandLiftStudyCell",
            "AdFreeFormCell",
            "AdGalleryCell",
            "AdLlmPostSuggestionsCell",
            "AdMetadataCell",
            "AdPromotedCommunityPostCell",
            "AdSpotlightVideoCell",
            "AdSupplementaryTextCell",
            "AppInstallCallToActionCell",
            "CallToActionCell",
            "MerchandisingUnitCell"
    );

    private static final HashSet<String> blockedHosts = new HashSet<>(
            Set.of(
                    "alb.reddit.com",
                    "e.reddit.com",
                    "w3-reporting.reddit.com",
                    "api2.branch.io"
            )
    );

    private static boolean hasBlockedHosts(Request request) {
        String host = request.url().host();
        return blockedHosts.contains(host);
    }

    private static boolean hasBlockedHeaders(Request request) {
        String apolloOperationName = request.header(APOLLO_OPERATION_HEADER);
        return PDP_COMMENTS_ADS_OP.equals(apolloOperationName);
    }

    private static JSONObject contextOf(JSONObject edge) {
        JSONObject node = edge.optJSONObject(NODE);
        return node == null ? null : node.optJSONObject(GROUP_RECOMMENDATION_CONTEXT);
    }

    private static boolean isAd(JSONObject edge) {
        JSONObject node = edge.optJSONObject(NODE);
        if (node == null) {
            return false;
        }
        boolean hasAdPayload = node.has(AD_PAYLOAD) && !node.isNull(AD_PAYLOAD);
        boolean hasAdCell = isAdCell(node);
        return hasAdPayload || hasAdCell;
    }

    private static boolean isAdCell(JSONObject node) {
        for (String nodeCell : nodeCells) {
            JSONArray cells = node.optJSONArray(nodeCell);
            if (cells != null) {
                for (int index = 0; index < cells.length(); index++) {
                    JSONObject cell = cells.optJSONObject(index);
                    if (cell != null) {
                        String cellTypeName = cell.optString(CELL_TYPE_NAME);
                        if (adCells.contains(cellTypeName)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private static boolean isGameRecommendation(JSONObject edge) {
        JSONObject context = contextOf(edge);
        if (context == null) {
            return false;
        }
        String typeIdentifier = context.optString(GROUP_RECOMMENDATION_TYPE_ID);
        return TYPE_ID_GAMES.equalsIgnoreCase(typeIdentifier);
    }

    private static boolean isFromSearch(JSONObject data) {
        JSONObject recommendation = data.optJSONObject(SEARCH_RECOMMENDATION);
        return recommendation != null && recommendation.has(SEARCH_TRENDING_QUERIES);
    }

    private static JSONArray filter(JSONArray items, AdFilter keep) throws JSONException {
        JSONArray result = new JSONArray();
        for (int index = 0; index < items.length(); index++) {
            JSONObject item = items.optJSONObject(index);
            if (item != null && keep.test(item)) {
                result.put(item);
            }
        }
        return result;
    }

    private static void removeFeedAds(JSONObject data) throws JSONException {
        for (String feed : feeds) {
            if (!data.has(feed)) {
                continue;
            }

            JSONObject elements = data.getJSONObject(feed).optJSONObject(ELEMENTS);
            JSONArray edges = elements == null ? null : elements.optJSONArray(EDGES);

            if (edges != null) {
                elements.put(EDGES, filter(edges, edge -> {
                    if (isAd(edge) || isGameRecommendation(edge)) {
                        return false;
                    }
                    removeRecommendationContextCell(edge);
                    return true;
                }));
            }
            break;
        }
    }

    private static void removeRecommendationContextCell(JSONObject edge) throws JSONException {
        if (!edge.has(NODE)) return;
        if (contextOf(edge) == null) return;

        JSONObject node = edge.getJSONObject(NODE);
        node.put(GROUP_RECOMMENDATION_CONTEXT, JSONObject.NULL);

        for (String nodeCell : nodeCells) {
            JSONArray cells = node.optJSONArray(nodeCell);
            if (cells != null) {
                node.put(nodeCell, filter(cells, cell -> {
                    String typeName = cell.optString(CELL_TYPE_NAME);
                    return !CELL_RECOMMENDATION_CONTEXT_CALL.equals(typeName);
                }));
            }
        }
    }

    private static void removeSearchAds(JSONObject data) throws JSONException {
        JSONObject trending = data
                .getJSONObject(SEARCH_RECOMMENDATION)
                .getJSONObject(SEARCH_TRENDING_QUERIES);
        JSONArray edges = trending.optJSONArray(EDGES);

        if (edges != null) {
            trending.put(EDGES, filter(edges, edge -> {
                if (!edge.has(NODE)) {
                    return false;
                }
                JSONObject node = edge.getJSONObject(NODE);
                return !node.optBoolean(SEARCH_IS_PROMOTED, false);
            }));
        }
    }

    public static boolean isRequestBlocked(Request request) {
        return hasBlockedHosts(request) || hasBlockedHeaders(request);
    }

    public static String removeAds(String jsonString) {
        try {
            JSONObject response = new JSONObject(jsonString);
            JSONObject data = response.optJSONObject(DATA);
            if (data != null) {
                if (isFromSearch(data)) {
                    removeSearchAds(data);
                } else {
                    removeFeedAds(data);
                }
            }
            return response.toString();
        } catch (JSONException error) {
            return jsonString;
        }
    }
}
