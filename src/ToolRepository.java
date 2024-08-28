class ToolRepository {
    public static ToolListing getToolListingByCode(String toolCode) {
        Tool tool = getToolByCode(toolCode);
        ToolChargeCategory toolChargeCategory = getToolChargeCategoryByType(tool.getToolType());
        return new ToolListing(tool, toolChargeCategory);
    }

    public static Tool getToolByCode(String toolCode) {
        return switch (toolCode) {
            case "CHNS" -> new Tool("CHNS", "Chainsaw", "Stihl");
            case "LADW" -> new Tool("LADW", "Ladder", "Werner");
            case "JAKD" -> new Tool("JAKD", "Jackhammer", "DeWalt");
            case "JAKR" -> new Tool("JAKR", "Jackhammer", "Ridgid");
            default -> throw new IllegalArgumentException("Invalid tool code");
        };
    }

    public static ToolChargeCategory getToolChargeCategoryByType(String toolType) {
        return switch (toolType) {
            case "Chainsaw" -> new ToolChargeCategory("Chainsaw", 1.49, true, false, true);
            case "Ladder" -> new ToolChargeCategory("Ladder", 1.99, true, true, false);
            case "Jackhammer" -> new ToolChargeCategory("Jackhammer", 2.99, true, false, false);
            default -> throw new IllegalArgumentException("Invalid tool type");
        };
    }
}