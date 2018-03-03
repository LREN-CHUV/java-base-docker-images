package eu.humanbrainproject.mip.algorithms.db;

/**
 * Exception related to a database operation
 *
 * @author Arnaud Jutzeler
 */
public class DBException extends Exception {

	private static final long serialVersionUID = -4203912300241196848L;

	private Exception parent;

    public DBException(Exception parent) {
        this.parent = parent;
    }

    @Override
    public String getMessage() {
        return parent.getMessage();
    }
}
