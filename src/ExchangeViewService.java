public class ExchangeViewService {

    // Calculator calculation logic
    public double evaluateExpression(String expression) {
        expression = expression.trim();
        String[] operands = expression.split("\\+|\\-|\\*|\\/");
        String operator = expression.replaceAll("[^\\+\\-\\*\\/]", "");
        double operand1 = Double.parseDouble(operands[0]);
        double operand2 = Double.parseDouble(operands[1]);

        double result = 0.0;

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

        return result;
    }
}
