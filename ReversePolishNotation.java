import java.util.*;

public class ReversePolishNotation {
    Stack<Double> numbers;
    Stack<String> result;

    String expression;
    double x;

    private enum Type {
        PLUS,
        MINUS,
        DIV,
        MULT,
        POW,
        MOD,
        COS,
        SIN,
        TAN,
        ATAN,
        ACOS,
        ASIN,
        SQRT,
        UPLUS,
        UMINUS,
        LOG,
        LN,
        X,
        DOT,
        CLOSE_BRACKET,
        OPEN_BRACKET,
        NUMBER
    }

    private enum Priority {
        LOW,
        MID,
        HIGH,
        VHIGH,
        VVHIGH
    }

    public ReversePolishNotation(String expression, double x) {
        this.expression = expression;
        this.x = x;
    }

    public ReversePolishNotation(String expression) {
        this.expression = expression;
        this.x = 0;
    }

    public double getResult() {
        return calculateRPN(getReversePolishNotation());
    }

    private String getReversePolishNotation() {
        String[] tokens = expression.split("(?=[-+*/^()])|(?<=[-+*/^()])");
        tokens = Arrays.stream(tokens)
                .map(String::trim)
                .filter(token -> !token.isEmpty())
                .toArray(String[]::new);
        numbers = new Stack<>();
        result = new Stack<>(); // Initialize the result stack

        StringBuilder rpnExpression = new StringBuilder();
        for (String token : tokens) {
            Type typeLexeme = getType(token);
            if (typeLexeme.compareTo(Type.PLUS) >= 0 && typeLexeme.compareTo(Type.LN) <= 0) {
                while (!result.isEmpty() && getPriority(typeLexeme).compareTo(getPriority(getType(result.peek()))) <= 0) {
                    rpnExpression.append(result.pop()).append(" ");
                }
                result.push(token);
            } else if (typeLexeme.equals(Type.NUMBER) || typeLexeme.equals(Type.X)) {
                rpnExpression.append(token).append(" ");
            } else if (typeLexeme.equals(Type.OPEN_BRACKET)) {
                result.push(token);
            } else if (typeLexeme.equals(Type.CLOSE_BRACKET)) {
                while (!result.isEmpty() && !getType(result.peek()).equals(Type.OPEN_BRACKET)) {
                    rpnExpression.append(result.pop()).append(" ");
                }
                if (!result.isEmpty() && getType(result.peek()).equals(Type.OPEN_BRACKET)) {
                    result.pop();
                }
            } else if (typeLexeme.equals(Type.POW)) {
                result.push(token);
            }
        }

        while (!result.isEmpty()) {
            rpnExpression.append(result.pop()).append(" ");
        }

        System.out.println("RPN Expression: " + rpnExpression);

        return rpnExpression.toString();
    }

    private Type getType(String token) {
        switch (token) {
            case "+": return Type.PLUS;
            case "-": return Type.MINUS;
            case "*": return Type.MULT;
            case "/": return Type.DIV;
            case "^": return Type.POW;
            case "mod": return Type.MOD;
            case "cos": return Type.COS;
            case "sin": return Type.SIN;
            case "tan": return Type.TAN;
            case "atan": return Type.ATAN;
            case "acos": return Type.ACOS;
            case "asin": return Type.ASIN;
            case "sqrt": return Type.SQRT;
            case "u+": return Type.UPLUS;
            case "u-": return Type.UMINUS;
            case "log": return Type.LOG;
            case "ln": return Type.LN;
            case "x": return Type.X;
            case ".": return Type.DOT;
            case ")": return Type.CLOSE_BRACKET;
            case "(": return Type.OPEN_BRACKET;
            default: return Type.NUMBER;
        }
    }

    private Priority getPriority(Type typeToken) {
        Priority priority = null;
        if (typeToken.equals(Type.PLUS) || typeToken.equals(Type.MINUS)) {
            priority = Priority.LOW;
        } else if (typeToken.equals(Type.DIV) || typeToken.equals(Type.MULT) || typeToken.equals(Type.MOD)) {
            priority = Priority.MID;
        } else if (typeToken.equals(Type.POW)) {
            priority = Priority.HIGH;
        } else if (typeToken.equals(Type.UMINUS) || typeToken.equals(Type.UPLUS)) {
            priority = Priority.VHIGH;
        } else if (typeToken.compareTo(Type.COS) >= 0 && typeToken.compareTo(Type.LN) <= 0 && (typeToken != Type.UPLUS && typeToken != Type.UMINUS)) {
            priority = Priority.VVHIGH;
        }
        return priority;
    }

    public double calculateRPN(String rpnExpression) {
        String[] tokens = rpnExpression.trim().split(" ");
        numbers = new Stack<>();

        for (String token : tokens) {
            if (Character.isDigit(token.charAt(0))) {
                numbers.push(Double.parseDouble(token));
            } else if (token.equals("x")) {
                numbers.push(x);
            } else {
                double operand2 = numbers.pop();
                double operand1 = numbers.pop();
                double result = applyOperator(token, operand1, operand2);
                numbers.push(result);
            }
        }

        return numbers.pop();
    }

    private double applyOperator(String operator, double operand1, double operand2) {
        switch (operator) {
            case "+":
                return operand1 + operand2;
            case "-":
                return operand1 - operand2;
            case "*":
                return operand1 * operand2;
            case "/":
                return operand1 / operand2;
            case "^":
                return Math.pow(operand1, operand2);
            case "mod":
                return operand1 % operand2;
            case "u+":
                return operand2;
            case "u-":
                return -operand2;
            default:
                throw new IllegalArgumentException("Invalid operator: " + operator);
        }
    }
}