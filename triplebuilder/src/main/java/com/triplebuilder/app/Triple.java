package com.triplebuilder.app;


import com.triplebuilder.app.Entity;

import java.util.List;
import java.util.ArrayList;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ie.util.RelationTriple;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphEdge;


public class Triple {
    private static String defaultRelation = "compound";

    private double confidence;
    private Entity subject;
    private Entity object;
    private String relation;
    private SemanticGraph graph;

    public static String getHeader(String path) {
        return "# " + path + "\n" + "confidence;subject;relation;object;subject_id;object_id";
    }

    public Triple(RelationTriple triple, Entity subject, Entity object) {
        this.confidence = triple.confidence;
        this.subject = subject;
        this.object = object;
        this.relation = triple.relationGloss();
        this.graph = triple.asDependencyTree().orElse(null);
    }

    
    public Triple(Entity subject, String relation, Entity object) {
        this.confidence = 1.0;
        this.subject = subject;
        this.object = object;
        this.relation = relation;
        this.graph = null;
    }

    public boolean subjectAndObjectAreDifferent() {
        return !this.subject.equals(this.object);
    }

    public List<Triple> buildAllTriples() {
        List<Triple> allTriples = new ArrayList<>();
        allTriples.add(this);
        allTriples.addAll(this.subject.buildDerivativeTriples());
        allTriples.addAll(this.object.buildDerivativeTriples());
        return allTriples;
    }

    public boolean notEmpty() {
        return !this.subject.cleanForm().getTokens().isEmpty() && !this.object.cleanForm().getTokens().isEmpty();
    }

    @Override
    public String toString() {
        Entity finalSubject = this.subject.cleanForm();
        Entity finalObject = this.object.cleanForm();
        return this.confidence + ";\"" + finalSubject.getText() + "\";\"" + this.relation + "\";\"" + finalObject.getText() + "\";\"" + finalSubject.getId() + "\";\"" + finalObject.getId() + "\"";
    }

    public static String findRelation(Entity subject, Entity object, SemanticGraph graph) {
        String relation = null;
        List<CoreLabel> subjectTokens = subject.getTokens();
        List<CoreLabel> objectTokens = object.getTokens();

        for (SemanticGraphEdge edge : graph.edgeIterable()) {
            CoreLabel source = edge.getSource().backingLabel();
            CoreLabel target = edge.getTarget().backingLabel();
            if (subjectTokens.contains(source) && objectTokens.contains(target)) {
                relation = edge.getRelation().getShortName();
                break;
            }
        }
        return relation;
    }
    
    public static Triple buildTriple(Entity subject, Entity object, SemanticGraph graph) {
        String relation = findRelation(subject, object, graph);

        if (relation == null) {
            return null;
        }

        switch (relation) {
            case "acl":
            case "amod":
            case "appos":
            case "obl:npmod":
                return new Triple(subject, "is", object);
            case "advmod":
            case "advmod:emph":
            case "advmod:lmod":
            case "csubj":
            case "csubj:pass":
            case "goeswith":
            case "nsubj":
            case "nsubj:pass":
            case "obj":
            case "obl":
            case "obl:arg":
            case "obl:lmod":
            case "obl:tmod":
            case "xcomp":
                return new Triple(subject, "at", object);
            case "compound":
            case "nmod":
                return new Triple(subject, "of", object);
            case "conj":
                return new Triple(subject, "and", object);
            case "nmod:poss":
            case "nmod:tmod":
                return new Triple(object, "has", subject);
            case "nummod":
            case "nummod:gov":
                return new Triple(subject, "amount to", object);
            case "obl:agent":
                return new Triple(subject, "by", object);
            case "vocative":
                return new Triple(object, "referenced by", subject);
            case "acl:relcl":
            case "advcl":
            case "aux":
            case "aux:pass":
            case "case":
            case "cc":
            case "cc:preconj":
            case "ccomp":
            case "clf":
            case "compound:lvc":
            case "compound:prt":
            case "compound:redup":
            case "compound:svc":
            case "cop":
            case "dep":
            case "det":
            case "det:numgov":
            case "det:nummod":
            case "det:poss":
            case "discourse":
            case "dislocated":
            case "expl":
            case "expl:impers":
            case "expl:pass":
            case "expl:pv":
            case "fixed":
            case "flat":
            case "flat:foreign":
            case "flat:name":
            case "iobj":
            case "list":
            case "mark":
            case "orphan":
            case "parataxis":
            case "punct":
            case "reparandum":
            case "root":
            default:
                return new Triple(subject, relation, object);
        }
    }
}
