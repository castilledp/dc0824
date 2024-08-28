class ToolListing {
    private final Tool tool;
    private final ToolChargeCategory toolChargeCategory;

    public ToolListing(Tool tool, ToolChargeCategory toolChargeCategory) {
        this.tool = tool;
        this.toolChargeCategory = toolChargeCategory;
    }

    public Tool getTool() {
        return tool;
    }

    public ToolChargeCategory getToolChargeCategory() {
        return toolChargeCategory;
    }
}
