package dev.jkcarino.extension.reddit.frontpage;

import android.view.View;

@SuppressWarnings("unused")
public final class DisablePostDetailSwipePatch {

    public static void apply(View view) {
        view.setTag("post_detail");
    }

    public static boolean isSwipeEnabled(View view){
        return view.isEnabled() && view.getTag() == null;
    }
}
