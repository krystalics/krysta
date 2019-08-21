package classreader;

import java.util.List;

public class ClassAnnotation {
    private String className;
    private List<String> annotations;

    public ClassAnnotation(String className, List<String> annotations) {
        this.className = className;
        this.annotations = annotations;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<String> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(List<String> annotations) {
        this.annotations = annotations;
    }
}
