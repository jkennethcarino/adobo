package dev.jkcarino.extension.reddit.frontpage;

@SuppressWarnings("unused")
public final class CommentIndentColorPatch {

    private static final long[] COLORS = {
            0xFF2A9D8FL, 0xFFC96B2CL,
            0xFF7A8F3AL, 0xFF8C4A5BL,
            0xFF4A6FA5L, 0xFFB85C38L,
            0xFF6B7A2BL, 0xFF7B5E8EL
    };

    private static final int COMPOSE_COLOR_SHIFT = 32;

    public static long getColorForDepth(int depth) {
        if (depth <= 0) {
            return COLORS[0] << COMPOSE_COLOR_SHIFT;
        }

        int index = (depth - 1) % COLORS.length;
        return COLORS[index] << COMPOSE_COLOR_SHIFT;
    }
}
