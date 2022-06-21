package intelligent.web.visitor;

import org.apache.commons.lang3.StringUtils;
import org.semanticweb.owlapi.model.*;

import java.util.List;

public class PrettyPrinter implements OWLClassExpressionVisitorEx<String> {
    private final static PrettyPrinter prettyPrinter = new PrettyPrinter();
    @Override
    public String visit(OWLObjectIntersectionOf ce) {
        List<String> collect = ce.operands()
                .map(o -> o.accept(this)).toList();
        return StringUtils.join(collect, ", ");
    }

    @Override
    public String visit(OWLObjectUnionOf ce) {
        List<String> collect = ce.operands()
                .map(o -> o.accept(this))
                .toList();
        return "(" + StringUtils.join(collect, " ∨ ") + ")";
    }

    @Override
    public String visit(OWLObjectComplementOf ce) {
        return "¬".concat(ce.getOperand().accept(this));
    }

    @Override
    public String visit(OWLObjectSomeValuesFrom ce) {
        return "∃".concat(((OWLObjectProperty)ce.getProperty()).getIRI().getShortForm())
                .concat(".")
                .concat(ce.getFiller().accept(this));
    }

    @Override
    public String visit(OWLObjectAllValuesFrom ce) {
        return "∀".concat(((OWLObjectProperty)ce.getProperty()).getIRI().getShortForm())
                .concat(".")
                .concat(ce.getFiller().accept(this));
    }

    @Override
    public String visit(OWLClass ce) {
        return ce.getIRI().getShortForm();
    }

    public static String printOwlExpression(OWLClassExpression owlClassExpression){
        return owlClassExpression.accept(prettyPrinter);
    }
}