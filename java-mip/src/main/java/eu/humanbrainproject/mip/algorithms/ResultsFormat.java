package eu.humanbrainproject.mip.algorithms;

/**
 * As the algorithm results are exported as a generic text, we define here what was the format used to encode the results.
 */
public enum ResultsFormat {
    ERROR("error", "text/plain+error"),
    /**
     * Portable Format for Analytics (PFA)
     * @see http://dmg.org/pfa/
     */
    PFA_JSON("pfa_json", "application/pfa+json"),
    /**
     * PFA encoded in Yaml
     * @see http://dmg.org/pfa/
     */
    PFA_YAML("pfa_yaml", "application/pfa+yaml"),
    HIGHCHARTS("highcharts_json", "application/vnd.highcharts+json"),
    JAVASCRIPT_VISJS("visjs", "application/vnd.visjs+javascript"),
    PLOTLY("plotly_json", "application/vnd.plotly.v1+json"),
    VEGA("vega_json", "application/vnd.vega+json"),
    VEGALITE("vega_lite_json", "application/vnd.vegalite+json"),
    PNG_IMAGE("png_image", "image/png;base64"),
    SVG_IMAGE("svg_image", "image/svg+xml"),
    HTML("html", "text/html"),
    /**
     * Tabular data resource
     * @see https://frictionlessdata.io/specs/tabular-data-resource/
     */
    TABULAR_DATA_RESOURCE_JSON("tabular_data_resource_json", "application/vnd.dataresource+json"),
    /**
     * Generic Json, for other types of visualisations
     */
    JSON("json", "application/json"),
    /**
     * Serialization of an object in Java using Java serialization
     */
    JAVA_SERIALIZATION("java_serialization", "application/java-serialized-object;base64");


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
