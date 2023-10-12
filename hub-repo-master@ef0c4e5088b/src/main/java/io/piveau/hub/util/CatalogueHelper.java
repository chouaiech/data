package io.piveau.hub.util;

import io.piveau.dcatap.DCATAPUriRef;
import io.piveau.dcatap.DCATAPUriSchema;
import io.piveau.utils.JenaUtils;
import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.util.ResourceUtils;
import org.apache.jena.vocabulary.DCAT;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.RDF;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class CatalogueHelper {
    private final String id;
    private final Resource catalogue;

    private final Model model;

    public CatalogueHelper(String id, String contentType, String content) {
        this.id = id;
        model = JenaUtils.read(content.getBytes(), contentType);

        renameReferences();
        catalogue = model.getResource(uriRef());

        modified();
        catalogue.addProperty(DCTerms.issued, ZonedDateTime
                .now(ZoneOffset.UTC)
                .truncatedTo(ChronoUnit.SECONDS)
                .format(DateTimeFormatter.ISO_DATE_TIME), XSDDatatype.XSDdateTime);
    }

    public Model getModel() {
        return model;
    }

    public String getId() {
        return id;
    }

    public String uriRef() {
        return DCATAPUriSchema.createForCatalogue(id).getUri();
    }

    public Resource resource() {
        return model.getResource(uriRef());
    }

    public void modified() {
        catalogue.removeAll(DCTerms.modified);
        catalogue.addProperty(DCTerms.modified, ZonedDateTime
                .now(ZoneOffset.UTC)
                .truncatedTo(ChronoUnit.SECONDS)
                .format(DateTimeFormatter.ISO_DATE_TIME), XSDDatatype.XSDdateTime);
    }

    public void addDataset(String uriRef) {
        DCATAPUriRef schema = DCATAPUriSchema.parseUriRef(uriRef);
        catalogue.addProperty(DCAT.dataset, model.createResource(schema.getDatasetUriRef()));
        catalogue.addProperty(DCAT.record, model.createResource(schema.getRecordUriRef()));
    }


    private void renameReferences() {
        model.listSubjectsWithProperty(RDF.type, DCAT.Catalog).forEachRemaining(ds ->
                ResourceUtils.renameResource(ds, uriRef()));
    }
}
