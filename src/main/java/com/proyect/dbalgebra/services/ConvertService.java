package com.proyect.dbalgebra.services;

import org.springframework.stereotype.Service;

import com.proyect.dbalgebra.entities.AlgebraRespose;
import com.proyect.dbalgebra.helpers.AlgebraOperators;
import com.proyect.dbalgebra.helpers.SQLoperators;

@Service
public class ConvertService {
       public AlgebraRespose convert(String relationalInput) throws Exception {
        String selectionFields = "*";
        String fromTables = "";
        String whereConditions = "";

        String unionSelect = "";
        String unionFields = "*";
        String unionTables = "";
        String unionWhereConditions = "";
        boolean currentlyInUnionOperation = false;

        String diffSelect = "";
        String diffFields = "*";
        String diffTables = "";
        String diffWhereConditions = "";
        boolean currentlyInDiffOperation = false;
        boolean currentlyInInterOperation = false;

        String joinTable = "";
        String joinOnCondition = "";
        String innerJoin = "INNER JOIN";
        boolean currentlyInJoinCondition = false;

        int i = 0;
        while (i < relationalInput.length()) {

            char current = relationalInput.charAt(i);
            // σ
            if (current == AlgebraOperators.SELECTION.symbol()) {
                String whereConditionsToSnipFromInput = relationalInput.substring(i + 1,
                        relationalInput.indexOf("(", i));

                if(whereConditionsToSnipFromInput.length() == 0) {
                    return new AlgebraRespose("", "Debes selecionar al menos un campo", true);
                }
                
                if (currentlyInUnionOperation) {
                    unionWhereConditions += SQLoperators.WHERE.symbol();
                    unionWhereConditions += " " + whereConditionsToSnipFromInput;
                } else {
                    whereConditions += SQLoperators.WHERE.symbol();
                    whereConditions += " " + whereConditionsToSnipFromInput;
                }

                i = relationalInput.indexOf("(", i);
                current = relationalInput.charAt(i);
            }

            // ∏
            if (current == AlgebraOperators.PROJECT.symbol()) {
                String fieldsToSnipFromInput = relationalInput.substring(i + 1, relationalInput.indexOf("(", i));
                
                if(fieldsToSnipFromInput.length() == 0) {
                    return new AlgebraRespose("", "Debes de proyectar al menos un campo", true);
                }
                
                if (currentlyInDiffOperation || currentlyInInterOperation) {
                    diffFields = "";
                    diffFields += fieldsToSnipFromInput;
                }

                if (currentlyInUnionOperation) {
                    unionFields = "";
                    if (unionFields.length() > 0) {
                        unionFields += "," + fieldsToSnipFromInput;
                    } else {
                        unionFields += fieldsToSnipFromInput;
                    }
                }

                if (!currentlyInDiffOperation && !currentlyInUnionOperation && !currentlyInInterOperation) {

                    if(selectionFields == "*") {
                        selectionFields = "";
                    }
                    
                    if (selectionFields.length() > 0) {
                        selectionFields += "," + fieldsToSnipFromInput;
                    } else {
                        selectionFields += fieldsToSnipFromInput;
                    }
                }

                i = relationalInput.indexOf("(", i);
                current = relationalInput.charAt(i);
            }

            // ∪
            if (current == AlgebraOperators.UNION.symbol()) {
                currentlyInUnionOperation = true;
                unionSelect += "UNION ALL SELECT";
            }

            // Χ
            if (current == AlgebraOperators.CARTESIAN.symbol()) {
                if (currentlyInUnionOperation) {
                    unionTables += ",";
                } else {
                    fromTables += ",";
                }

            }

            // ∩
            if (current == AlgebraOperators.INTERSECT.symbol()) {
                whereConditions += "WHERE " + "EXISTS(";
                diffSelect += SQLoperators.SELECT;
                currentlyInInterOperation = true;
            }

            // -
            if (current == AlgebraOperators.DIFF.symbol()) {
                whereConditions += "WHERE " + "NOT EXISTS(";
                diffSelect += SQLoperators.SELECT;
                currentlyInDiffOperation = true;
            }

            if(current == AlgebraOperators.REUNION.symbol()) {
                String onConditionsToSnipFromInput = relationalInput.substring(i + 1,
                        relationalInput.indexOf("(", i));

                if(onConditionsToSnipFromInput.length() == 0) {
                    return new AlgebraRespose("", "Debes proporcionar los campos para realizar la reunion", true);
                }

                currentlyInJoinCondition = true;
                joinOnCondition += onConditionsToSnipFromInput;
                i = relationalInput.indexOf("(", i);
                current = relationalInput.charAt(i);
            }

            if (current != '(' &&
                    current != ')' &&
                    current != AlgebraOperators.CARTESIAN.symbol() &&
                    current != AlgebraOperators.UNION.symbol() &&
                    current != AlgebraOperators.DIFF.symbol() &&
                    current != AlgebraOperators.INTERSECT.symbol()) {

                if (currentlyInDiffOperation || currentlyInInterOperation) {
                    diffTables += current;
                }

                if (currentlyInUnionOperation) {
                    unionTables += current;
                }

                if(currentlyInJoinCondition) {
                    joinTable += current;
                }

                if (
                    !currentlyInDiffOperation && 
                    !currentlyInUnionOperation && 
                    !currentlyInInterOperation && 
                    !currentlyInJoinCondition
                ) {
                    fromTables += current;
                }

            }

            i++;
        }

        String result = SQLoperators.SELECT + " " + selectionFields + " " + SQLoperators.FROM
                + " " + fromTables;


        if(fromTables.length() == 0) {
            return new AlgebraRespose("", "Debes proporcionar las tablas para operar", true);
        }

        if(joinTable.length() > 0) {
            result += " " + innerJoin + " " + joinTable + " " + "ON" + " " + joinOnCondition;
        }

        if (whereConditions.length() > 0) {
            result += " " + whereConditions;
        }

        if (unionSelect.length() > 0) {

            if(unionTables.length() == 0) {
                return new AlgebraRespose("", "Para realizar la union necesitas proporcionar las tablas", true);
            }

            result += " " + unionSelect + " " + unionFields + " " + SQLoperators.FROM + " " + unionTables + " "
                    + unionWhereConditions;
        }

        if (diffSelect.length() > 0) {
            String fieldsForDiffToCompare[] = diffFields.split(",");
            String fieldsForGeneralToCompare[] = selectionFields.split(",");

            if (fieldsForDiffToCompare.length != fieldsForGeneralToCompare.length) {
                return new AlgebraRespose("", "Para realizar la resta/interseccion debe de proyectar los mismos atributos", true);
            }

            diffWhereConditions += "WHERE";

            for (String c : fieldsForGeneralToCompare) {
                diffWhereConditions += " " + fromTables + "." + c + "=" + diffTables + "." + c;
            }

            diffWhereConditions += ")";

            result += " " + diffSelect + " " + diffFields + " " + SQLoperators.FROM + " " + diffTables + " "
                    + diffWhereConditions;
        }

        result += ";";

        return new AlgebraRespose(
            result.trim(),
            "",
            false
        );
    }
}
