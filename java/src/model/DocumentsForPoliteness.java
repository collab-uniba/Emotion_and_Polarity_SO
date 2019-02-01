package model;

import java.util.List;

public class DocumentsForPoliteness {

    private List<DocumentForPoliteness> documentsForPolitenesses;

    public DocumentsForPoliteness(List<DocumentForPoliteness> documentsForPolitenesses) {
        this.documentsForPolitenesses = documentsForPolitenesses;
    }

    public List<DocumentForPoliteness> getDocumentsForPolitenesses() {
        return documentsForPolitenesses;
    }

    public void setDocumentsForPolitenesses(List<DocumentForPoliteness> documentsForPolitenesses) {
        this.documentsForPolitenesses = documentsForPolitenesses;
    }
}
