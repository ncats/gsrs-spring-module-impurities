package gov.nih.ncats.impurities.indexers;

import gov.nih.ncats.impurities.models.Impurities;

import ix.core.search.text.IndexValueMaker;
import ix.core.search.text.IndexableValue;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ImpuritiesIndexValueMaker implements IndexValueMaker<Impurities> {
    private static final Pattern SPLIT = Pattern.compile("(\\s+)");
    @Override
    public Class<Impurities> getIndexedEntityClass() {
        return Impurities.class;
    }

    @Override
    public void createIndexableValues(Impurities impurities, Consumer<IndexableValue> consumer) {
        Long id = impurities.id;
        //if(title ==null){
        //    return;
       // }

      //  consumer.accept(IndexableValue.simpleFacetLongValue("application.id", id));
        /*
        Matcher matcher = SPLIT.matcher(title);
        if(matcher.find()){
            String firstName = title.substring(0, matcher.start(1));
            String lastName = title.substring(matcher.end(1));
            consumer.accept(IndexableValue.simpleFacetStringValue("author.firstName", firstName));
            consumer.accept(IndexableValue.simpleFacetStringValue("author.lastName", lastName));
        }
         */
    }
}
