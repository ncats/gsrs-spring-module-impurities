package gov.hhs.gsrs.impurities.exporters;

import gov.hhs.gsrs.impurities.controllers.ImpuritiesController;
import gov.hhs.gsrs.impurities.models.*;

import ix.ginas.exporters.*;
import gsrs.springUtils.AutowireHelper;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.JsonNode;

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
    TOTAL_IMPURITIES_TEST_TYPE,
    TOTAL_IMPURITIES_LIMIT_VALUE,
    TOTAL_IMPURITIES_AMOUNT_VALUE,
    TOTAL_IMPURITIES_LIMIT_TYPE,
    TOTAL_IMPURITIES_COMMENTS,
    APPROVAL_ID,
    SUBSTANCE_NAME
}

public class ImpuritiesExporter implements Exporter<Impurities> {

    @Autowired
    public static ImpuritiesController impuritiesController;

    private final Spreadsheet spreadsheet;

    private int row = 1;

    private final List<ColumnValueRecipe<Impurities>> recipeMap;

    private static StringBuilder substanceApprovalIdSB;

    private ImpuritiesExporter(Builder builder, ImpuritiesController impuritiesController) {

        this.impuritiesController = impuritiesController;
        substanceApprovalIdSB = new StringBuilder();

        this.spreadsheet = builder.spreadsheet;
        this.recipeMap = builder.columns;

        int j = 0;
        Spreadsheet.SpreadsheetRow header = spreadsheet.getRow(0);
        for (ColumnValueRecipe<Impurities> col : recipeMap) {
            j += col.writeHeaderValues(header, j);
        }
    }

    @Override
    public void export(Impurities s) throws IOException {
        Spreadsheet.SpreadsheetRow row = spreadsheet.getRow(this.row++);

        int j = 0;
        for (ColumnValueRecipe<Impurities> recipe : recipeMap) {
            j += recipe.writeValuesFor(row, j, s);
        }
    }

    @Override
    public void close() throws IOException {
        spreadsheet.close();
    }

    private static Map<Column, ColumnValueRecipe<Impurities>> DEFAULT_RECIPE_MAP;

    static {

        DEFAULT_RECIPE_MAP = new LinkedHashMap<>();

        DEFAULT_RECIPE_MAP.put(ImpDefaultColumns.SUBSTANCE_NAME, SingleColumnValueRecipe.create(ImpDefaultColumns.SUBSTANCE_NAME, (s, cell) -> {
            StringBuilder sb = getIngredientDetails(s, ImpDefaultColumns.SUBSTANCE_NAME);
            cell.writeString(sb.toString());
        }));

        DEFAULT_RECIPE_MAP.put(ImpDefaultColumns.APPROVAL_ID, SingleColumnValueRecipe.create(ImpDefaultColumns.APPROVAL_ID, (s, cell) -> {
            cell.writeString(substanceApprovalIdSB.toString());
        }));

        DEFAULT_RECIPE_MAP.put(ImpDefaultColumns.SOURCE_TYPE, SingleColumnValueRecipe.create(ImpDefaultColumns.SOURCE_TYPE, (s, cell) -> cell.writeString(s.sourceType)));
        DEFAULT_RECIPE_MAP.put(ImpDefaultColumns.SOURCE, SingleColumnValueRecipe.create(ImpDefaultColumns.SOURCE, (s, cell) -> cell.writeString(s.source)));
        DEFAULT_RECIPE_MAP.put(ImpDefaultColumns.Source_ID, SingleColumnValueRecipe.create(ImpDefaultColumns.Source_ID, (s, cell) -> cell.writeString(s.sourceId)));
        DEFAULT_RECIPE_MAP.put(ImpDefaultColumns.TYPE, SingleColumnValueRecipe.create(ImpDefaultColumns.TYPE, (s, cell) -> cell.writeString(s.type)));
        DEFAULT_RECIPE_MAP.put(ImpDefaultColumns.SPEC_TYPE, SingleColumnValueRecipe.create(ImpDefaultColumns.SPEC_TYPE, (s, cell) -> cell.writeString(s.specType)));
        DEFAULT_RECIPE_MAP.put(ImpDefaultColumns.PRODUCT_ID, SingleColumnValueRecipe.create(ImpDefaultColumns.PRODUCT_ID, (s, cell) -> cell.writeString(s.productId)));
        DEFAULT_RECIPE_MAP.put(ImpDefaultColumns.COMPANY_PRODUCT_NAME, SingleColumnValueRecipe.create(ImpDefaultColumns.COMPANY_PRODUCT_NAME, (s, cell) -> cell.writeString(s.companyProductName)));
        DEFAULT_RECIPE_MAP.put(ImpDefaultColumns.COMPANY_NAME, SingleColumnValueRecipe.create(ImpDefaultColumns.COMPANY_NAME, (s, cell) -> cell.writeString(s.companyName)));

        DEFAULT_RECIPE_MAP.put(ImpDefaultColumns.TOTAL_IMPURITIES_TEST_TYPE, SingleColumnValueRecipe.create(ImpDefaultColumns.TOTAL_IMPURITIES_TEST_TYPE, (s, cell) -> {
            if (s.impuritiesTotal != null ) {
                cell.writeString(s.impuritiesTotal.testType);
            }
        }));

        DEFAULT_RECIPE_MAP.put(ImpDefaultColumns.TOTAL_IMPURITIES_LIMIT_TYPE, SingleColumnValueRecipe.create(ImpDefaultColumns.TOTAL_IMPURITIES_LIMIT_TYPE, (s, cell) -> {
            if (s.impuritiesTotal != null ) {
                cell.writeString(s.impuritiesTotal.limitType);
            }
        }));

        DEFAULT_RECIPE_MAP.put(ImpDefaultColumns.TOTAL_IMPURITIES_LIMIT_VALUE, SingleColumnValueRecipe.create(ImpDefaultColumns.TOTAL_IMPURITIES_LIMIT_VALUE, (s, cell) -> {
            if (s.impuritiesTotal != null ) {
                cell.writeString(s.impuritiesTotal.limitValue);
            }
        }));

        DEFAULT_RECIPE_MAP.put(ImpDefaultColumns.TOTAL_IMPURITIES_AMOUNT_VALUE, SingleColumnValueRecipe.create(ImpDefaultColumns.TOTAL_IMPURITIES_AMOUNT_VALUE, (s, cell) -> {
            if (s.impuritiesTotal != null ) {
                cell.writeString(s.impuritiesTotal.amountValue);
            }
        }));

    }

    private static StringBuilder getIngredientDetails(Impurities s, ImpDefaultColumns fieldName) {
        StringBuilder sb = new StringBuilder();
        substanceApprovalIdSB.setLength(0);
        String substanceName = null;
        String approvalId = null;

        try {
            // Export Impurities Substance
            if (s.impuritiesSubstanceList.size() > 0) {
                List<ImpuritiesSubstance> subList = s.impuritiesSubstanceList;

                for (ImpuritiesSubstance impuritiesSub : subList) {

                    if (sb.length() != 0) {
                        sb.append("|");
                    }

                    if (substanceApprovalIdSB.length() != 0) {
                        substanceApprovalIdSB.append("|");
                    }

                    try {
                        if (impuritiesSub.substanceUuid != null) {
                            if (impuritiesController != null) {
                                JsonNode subJson = impuritiesController.injectSubstanceBySubstanceUuid(impuritiesSub.substanceUuid);

                                if (subJson != null) {
                                    substanceName = subJson.path("_name").textValue();
                                    approvalId = subJson.path("approvalID").textValue();

                                    // Get Substance Name
                                    sb.append((substanceName != null) ? substanceName : "(No Ingredient Name)");

                                    // Storing in static variable so do not have to call the same Substance API twice just to get
                                    // approval Id.
                                    substanceApprovalIdSB.append((approvalId != null) ? approvalId : "(No Approval ID)");
                                }
                            }
                        } else {
                            sb.append("(No Ingredient Name)");
                            substanceApprovalIdSB.append("(No Approval ID)");
                        }
                    } catch (Exception ex) {
                        System.out.println("*** Error Occured in Impurities Exporter ***");
                        ex.printStackTrace();
                    }
                }
            }
        } catch (
                Exception ex) {
            ex.printStackTrace();
        }
        return sb;
    }

    /**
     * Builder class that makes a SpreadsheetExporter.  By default, the default columns are used
     * but these may be modified using the add/remove column methods.
     */
    public static class Builder {
        private final List<ColumnValueRecipe<Impurities>> columns = new ArrayList<>();
        private final Spreadsheet spreadsheet;

        private boolean publicOnly = false;

        /**
         * Create a new Builder that uses the given Spreadsheet to write to.
         *
         * @param spreadSheet the {@link Spreadsheet} object that will be written to by this exporter. can not be null.
         * @throws NullPointerException if spreadsheet is null.
         */
        public Builder(Spreadsheet spreadSheet) {
            Objects.requireNonNull(spreadSheet);
            this.spreadsheet = spreadSheet;

            for (Map.Entry<Column, ColumnValueRecipe<Impurities>> entry : DEFAULT_RECIPE_MAP.entrySet()) {
                columns.add(entry.getValue());
            }
        }

        public Builder addColumn(Column column, ColumnValueRecipe<Impurities> recipe) {
            return addColumn(column.name(), recipe);
        }

        public Builder addColumn(String columnName, ColumnValueRecipe<Impurities> recipe) {
            Objects.requireNonNull(columnName);
            Objects.requireNonNull(recipe);
            columns.add(recipe);

            return this;
        }

        public Builder renameColumn(Column oldColumn, String newName) {
            return renameColumn(oldColumn.name(), newName);
        }

        public Builder renameColumn(String oldName, String newName) {
            //use iterator to preserve order
            ListIterator<ColumnValueRecipe<Impurities>> iter = columns.listIterator();
            while (iter.hasNext()) {

                ColumnValueRecipe<Impurities> oldValue = iter.next();
                ColumnValueRecipe<Impurities> newValue = oldValue.replaceColumnName(oldName, newName);
                if (oldValue != newValue) {
                    iter.set(newValue);
                }
            }
            return this;
        }

        public ImpuritiesExporter build(ImpuritiesController impuritiesController) {
            return new ImpuritiesExporter(this, impuritiesController);
        }

        public Builder includePublicDataOnly(boolean publicOnly) {
            this.publicOnly = publicOnly;
            return this;
        }

    }
}