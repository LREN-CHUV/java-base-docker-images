package eu.humanbrainproject.mip.algorithms;

/**
 * As the algorithm results are exported as a generic text, we define here what was the format used to encode the results.
 */
public enum ResultsFormat {
    PFA_JSON("pfa_json", "application/pfa+json"),
    PFA_YAML("pfa_yaml", "application/pfa+yaml"),
    HIGHCHARTS("highcharts_json", "application/highcharts+json"),
    JAVASCRIPT_VISJS("visjs", "application/visjs+javascript"),
    PNG_IMAGE("png_image", "image/png;base64"),
    SVG_IMAGE("svg_image", "image/svg+xml");

    private final String shape;
    private final String mimeType;

    ResultsFormat(String shape, String mimeType) {
        this.shape = shape;
        this.mimeType = mimeType;
    }

    public String getShape() {
        return shape;
    }

    public String getMimeType() {
        return mimeType;
    }

}
