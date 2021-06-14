package gov.nih.ncats.impurities.exporters;

import gov.nih.ncats.impurities.controllers.ImpuritiesController;
import gov.nih.ncats.impurities.models.*;

// import ix.gsrs.substance.exporters.FDACodeExporter;
import ix.ginas.exporters.*;
import gsrs.springUtils.AutowireHelper;

import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.*;

enum ImpDefaultColumns implements Column {
    ID,
    SOURCE_TYPE,
    SOURCE,
    Source_ID,
    TYPE,
    SPEC_TYPE,
    PRODUCT_ID,
    COMPANY_PRODUCT_NAME,
    COMPANY_NAME,
    SUBSTANCE_KEY,
    APPROVAL_ID,
    SUBSTANCE_NAME
}

public class ImpuritiesExporter implements Exporter<Impurities> {

    @Autowired
    public static ImpuritiesController impuritiesController;

    private final Spreadsheet spreadsheet;

    private int row=1;

    private final List<ColumnValueRecipe<Impurities>> recipeMap;

    private ImpuritiesExporter(Builder builder){

        if(impuritiesController==null) {
            AutowireHelper.getInstance().autowire(this);
        }

        this.spreadsheet = builder.spreadsheet;
        this.recipeMap = builder.columns;

        int j=0;
        Spreadsheet.SpreadsheetRow header = spreadsheet.getRow(0);
        for(ColumnValueRecipe<Impurities> col : recipeMap){
            j+= col.writeHeaderValues(header, j);
        }
    }

    @Override
    public void export(Impurities s) throws IOException {
        Spreadsheet.SpreadsheetRow row = spreadsheet.getRow( this.row++);

        int j=0;
        for(ColumnValueRecipe<Impurities> recipe : recipeMap){
            j+= recipe.writeValuesFor(row, j, s);
        }
    }

    @Override
    public void close() throws IOException {
        spreadsheet.close();
    }

    private static Map<Column, ColumnValueRecipe<Impurities>> DEFAULT_RECIPE_MAP;

    static{

        DEFAULT_RECIPE_MAP = new LinkedHashMap<>();

        DEFAULT_RECIPE_MAP.put(ImpDefaultColumns.SUBSTANCE_NAME, SingleColumnValueRecipe.create( ImpDefaultColumns.SUBSTANCE_NAME ,(s, cell) ->{
        	StringBuilder sb = getIngredientDetails(s, ImpDefaultColumns.SUBSTANCE_NAME);
            cell.writeString(sb.toString());
        }));

        DEFAULT_RECIPE_MAP.put(ImpDefaultColumns.APPROVAL_ID, SingleColumnValueRecipe.create( ImpDefaultColumns.APPROVAL_ID ,(s, cell) ->{
            StringBuilder sb = getIngredientDetails(s, ImpDefaultColumns.APPROVAL_ID);
            cell.writeString(sb.toString());
        }));
/*
        DEFAULT_RECIPE_MAP.put(ImpDefaultColumns.BDNUM, SingleColumnValueRecipe.create( ImpDefaultColumns.BDNUM ,(s, cell) ->{
            StringBuilder sb = getIngredientDetails(s, ImpDefaultColumns.BDNUM);
            cell.writeString(sb.toString());
        }));

        DEFAULT_RECIPE_MAP.put(ImpDefaultColumns.INGREDIENT_TYPE, SingleColumnValueRecipe.create( ImpDefaultColumns.INGREDIENT_TYPE ,(s, cell) ->{
            StringBuilder sb = getIngredientDetails(s, ImpDefaultColumns.INGREDIENT_TYPE);
            cell.writeString(sb.toString());
        }));
        */

        DEFAULT_RECIPE_MAP.put(ImpDefaultColumns.SOURCE_TYPE, SingleColumnValueRecipe.create( ImpDefaultColumns.SOURCE_TYPE ,(s, cell) -> cell.writeString(s.sourceType)));
        DEFAULT_RECIPE_MAP.put(ImpDefaultColumns.SOURCE, SingleColumnValueRecipe.create( ImpDefaultColumns.SOURCE ,(s, cell) -> cell.writeString(s.source)));
        DEFAULT_RECIPE_MAP.put(ImpDefaultColumns.Source_ID, SingleColumnValueRecipe.create( ImpDefaultColumns.Source_ID ,(s, cell) -> cell.writeString(s.sourceId)));
        DEFAULT_RECIPE_MAP.put(ImpDefaultColumns.TYPE, SingleColumnValueRecipe.create( ImpDefaultColumns.TYPE ,(s, cell) -> cell.writeString(s.type)));
        DEFAULT_RECIPE_MAP.put(ImpDefaultColumns.SPEC_TYPE, SingleColumnValueRecipe.create( ImpDefaultColumns.SPEC_TYPE ,(s, cell) -> cell.writeString(s.specType)));
        DEFAULT_RECIPE_MAP.put(ImpDefaultColumns.PRODUCT_ID, SingleColumnValueRecipe.create( ImpDefaultColumns.PRODUCT_ID ,(s, cell) -> cell.writeString(s.productId)));
        DEFAULT_RECIPE_MAP.put(ImpDefaultColumns.COMPANY_PRODUCT_NAME, SingleColumnValueRecipe.create( ImpDefaultColumns.COMPANY_PRODUCT_NAME ,(s, cell) -> cell.writeString(s.companyProductName)));
        DEFAULT_RECIPE_MAP.put(ImpDefaultColumns.COMPANY_NAME, SingleColumnValueRecipe.create( ImpDefaultColumns.COMPANY_NAME,(s, cell) -> cell.writeString(s.companyName)));

        /*
        DEFAULT_RECIPE_MAP.put(ImpDefaultColumns.APP_NUMBER, SingleColumnValueRecipe.create( ImpDefaultColumns.APP_NUMBER ,(s, cell) -> cell.writeString(s.appNumber)));
        DEFAULT_RECIPE_MAP.put(ImpDefaultColumns.PRODUCT_NAME, SingleColumnValueRecipe.create( ImpDefaultColumns.PRODUCT_NAME ,(s, cell) ->{
            StringBuilder sb = getProductDetails(s, ImpDefaultColumns.PRODUCT_NAME);
            cell.writeString(sb.toString());
        }));
        DEFAULT_RECIPE_MAP.put(ImpDefaultColumns.SPONSOR_NAME, SingleColumnValueRecipe.create( ImpDefaultColumns.SPONSOR_NAME ,(s, cell) -> cell.writeString(s.sponsorName)));
        DEFAULT_RECIPE_MAP.put(ImpDefaultColumns.APP_STATUS, SingleColumnValueRecipe.create( ImpDefaultColumns.APP_STATUS ,(s, cell) -> cell.writeString(s.status)));
        DEFAULT_RECIPE_MAP.put(ImpDefaultColumns.APP_SUB_TYPE, SingleColumnValueRecipe.create( ImpDefaultColumns.APP_SUB_TYPE ,(s, cell) -> cell.writeString(s.appSubType)));
        DEFAULT_RECIPE_MAP.put(ImpDefaultColumns.DIVISION_CLASS_DESC, SingleColumnValueRecipe.create( ImpDefaultColumns.DIVISION_CLASS_DESC ,(s, cell) -> cell.writeString(s.divisionClassDesc)));
        DEFAULT_RECIPE_MAP.put(ImpDefaultColumns.PROVENANCE, SingleColumnValueRecipe.create( ImpDefaultColumns.PROVENANCE ,(s, cell) -> cell.writeString(s.provenance)));
        DEFAULT_RECIPE_MAP.put(ImpDefaultColumns.INDICATION, SingleColumnValueRecipe.create( ImpDefaultColumns.INDICATION ,(s, cell) ->{
            StringBuilder sb = getIndicationDetails(s);
            cell.writeString(sb.toString());
        }));

         */
    }

    private static StringBuilder getProductDetails(Impurities s, ImpDefaultColumns fieldName) {
        StringBuilder sb = new StringBuilder();
/*
        if(s.applicationProductList.size() > 0){
            List<ApplicationProduct> prodList = s.applicationProductList;

            for(ApplicationProduct prod : prodList){

                for (ApplicationProductName prodName : prod.applicationProductNameList) {
                    if(sb.length()!=0){
                        sb.append("|");
                    }
                    switch (fieldName) {
                        case PRODUCT_NAME:
                            sb.append((prodName.productName != null) ? prodName.productName : "(No Product Name)");
                            break;
                        default:
                            break;
                    }
                }
            }
        }

 */
        return sb;
    }

    private static StringBuilder getIngredientDetails(Impurities s, ImpDefaultColumns fieldName) {
        StringBuilder sb = new StringBuilder();

        try {

         //   Optional<Impurities> imp = impuritiesController.injectSubstanceDetails(Optional.of(s));

            if (s.impuritiesSubstanceList.size() > 0) {
                List<ImpuritiesSubstance> subList = s.impuritiesSubstanceList;

                for (ImpuritiesSubstance sList : subList) {

                        if (sb.length() != 0) {
                            sb.append("|");
                        }

                        try {
                            switch (fieldName) {
                                case SUBSTANCE_NAME:
                                    if (sList != null) {
                                    //    sb.append((sList._name != null) ? sList._name : "(No Ingredient Name)");
                                    } else {
                                        sb.append("(No Ingredient Name)");
                                    }
                                    break;
                                case APPROVAL_ID:
                                    if (sList != null) {
                                    //    sb.append((sList._approvalID != null) ? sList._approvalID : "(No Unii)");
                                    } else {
                                        sb.append("(No Unii)");
                                    }
                                    break;
                                    /*
                                case BDNUM:
                                    sb.append((ingred.bdnum != null) ? ingred.bdnum : "(No Bdnum)");
                                    break;
                                case INGREDIENT_TYPE:
                                    sb.append((ingred.ingredientType != null) ? ingred.ingredientType : "(No Ingredient Type)");
                                    break;
                                     */
                                default:
                                    break;
                            }
                        } catch (Exception ex) {
                            System.out.println("*** Error Occured in Impurities Exporter ***");
                            ex.printStackTrace();
                        }
                    // }
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return sb;
    }

    private static StringBuilder getIndicationDetails(Impurities s) {
        StringBuilder sb = new StringBuilder();
/*
        if(s.applicationIndicationList.size() > 0){
            List<ApplicationIndication> indList = s.applicationIndicationList;

            for(ApplicationIndication ind : indList){
                if(sb.length()!=0){
                    sb.append("|");
                }
                sb.append((ind.indication != null) ? ind.indication : "");
            }
        }

 */
        return sb;
    }

    /**
     * Builder class that makes a SpreadsheetExporter.  By default, the default columns are used
     * but these may be modified using the add/remove column methods.
     *
     */
    public static class Builder{
        private final List<ColumnValueRecipe<Impurities>> columns = new ArrayList<>();
        private final Spreadsheet spreadsheet;

        private boolean publicOnly = false;

        /**
         * Create a new Builder that uses the given Spreadsheet to write to.
         * @param spreadSheet the {@link Spreadsheet} object that will be written to by this exporter. can not be null.
         *
         * @throws NullPointerException if spreadsheet is null.
         */
        public Builder(Spreadsheet spreadSheet){
            Objects.requireNonNull(spreadSheet);
            this.spreadsheet = spreadSheet;

            for(Map.Entry<Column, ColumnValueRecipe<Impurities>> entry : DEFAULT_RECIPE_MAP.entrySet()){
            	columns.add(entry.getValue());
            }
        }

        public Builder addColumn(Column column, ColumnValueRecipe<Impurities> recipe){
        	return addColumn(column.name(), recipe);
        }

        public Builder addColumn(String columnName, ColumnValueRecipe<Impurities> recipe){
        	Objects.requireNonNull(columnName);
            Objects.requireNonNull(recipe);
            columns.add(recipe);

            return this;
        }

        public Builder renameColumn(Column oldColumn, String newName){
            return renameColumn(oldColumn.name(), newName);
        }
        
        public Builder renameColumn(String oldName, String newName){
            //use iterator to preserve order
            ListIterator<ColumnValueRecipe<Impurities>> iter = columns.listIterator();
            while(iter.hasNext()){

                ColumnValueRecipe<Impurities> oldValue = iter.next();
                ColumnValueRecipe<Impurities> newValue = oldValue.replaceColumnName(oldName, newName);
                if(oldValue != newValue){
                   iter.set(newValue);
                }
            }
            return this;
        }

        public ImpuritiesExporter build(){
            return new ImpuritiesExporter(this);
        }

        public Builder includePublicDataOnly(boolean publicOnly){
            this.publicOnly = publicOnly;
            return this;
        }

    }
}