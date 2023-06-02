public class ExchangeViewService {


    public double evaluateExpression(String expression) {
        double result = 0.0;
        String[] operands = expression.split("\\+|\\-|\\*|\\/");
        String operator = expression.replaceAll("[^\\+\\-\\*\\/]", "");
        double operand1 = Double.parseDouble(operands[0]);
        try {
            expression = expression.trim();

            double operand2 = Double.parseDouble(operands[1]);

            switch (operator) {
                case "+":
                    result = operand1 + operand2;
                    break;
                case "-":
                    result = operand1 - operand2;
                    break;
                case "*":
                    result = operand1 * operand2;
                    break;
                case "/":
                    result = operand1 / operand2;
                    break;
            }
        }catch (ArrayIndexOutOfBoundsException e){
            result = operand1;
        }

        return result;
    }
}
