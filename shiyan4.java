import java.util.*;
// 定义算式接口
interface IEquation {
    short getOperand1();
    short getOperand2();
    char getOperator();
    void setOperand1(short operand1);
    void setOperand2(short operand2);
    void setOperator(char operator);
    int calculate();
}

// 定义算式抽象类
abstract class AbstractEquation implements IEquation {
    protected short operand1;
    protected short operand2;
    protected char operator;
    @Override
    public short getOperand1() {
        return operand1;
    }
    @Override
    public short getOperand2() {
        return operand2;
    }
    @Override
    public char getOperator() {
        return operator;
    }
    @Override
    public void setOperand1(short operand1) {
        this.operand1 = operand1;
    }
    @Override
    public void setOperand2(short operand2) {
        this.operand2 = operand2;
    }
    @Override
    public void setOperator(char operator) {
        this.operator = operator;
    }
    @Override
    public int calculate() {
        if (operator == '+') {
            return operand1 + operand2;
        } else if (operator == '-') {
            return operand1 - operand2;
        } else {
            throw new UnsupportedOperationException("不支持的运算符 " + operator);
        }
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        AbstractEquation that = (AbstractEquation) obj;
        return operand1 == that.operand1 &&
                operand2 == that.operand2 &&
                operator == that.operator;
    }
    @Override
    public int hashCode() {
        return Objects.hash(operand1, operand2, operator);
    }
}

// 定义加法算式
class AddEquation extends AbstractEquation {
    public static class AddEquationBuilder {
        private AddEquation addEquation;

        public AddEquationBuilder() {
            addEquation = new AddEquation();
        }
        public AddEquationBuilder operand1(short operand1) {
            addEquation.setOperand1(operand1);
            return this;
        }
        public AddEquationBuilder operand2(short operand2) {
            addEquation.setOperand2(operand2);
            return this;
        }
        public AddEquation build() {
            return addEquation;
        }
    }
    @Override
    public int calculate() {
        return operand1 + operand2;
    }
}

// 定义减法算式
class SubEquation extends AbstractEquation {
    public static class SubEquationBuilder {
        private SubEquation subEquation;

        public SubEquationBuilder() {
            subEquation = new SubEquation();
        }
        public SubEquationBuilder operand1(short operand1) {
            subEquation.setOperand1(operand1);
            return this;
        }
        public SubEquationBuilder operand2(short operand2) {
            subEquation.setOperand2(operand2);
            return this;
        }
        public SubEquation build() {
            return subEquation;
        }
    }
    @Override
    public int calculate() {
        return operand1 - operand2;
    }
}

// 定义算式约束接口
interface EquationChecker {
    boolean check(IEquation equation);
}

// 定义算式范围约束实体类
class EquationCheckerOfRange implements EquationChecker {
    private int min;
    private int max;

    public EquationCheckerOfRange(int min, int max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public boolean check(IEquation equation) {
        return equation.getOperand1() >= min && equation.getOperand1() <= max
                && equation.getOperand2() >= min && equation.getOperand2() <= max
                && equation.calculate() >= min && equation.calculate() <= max;
    }
}

// 定义工厂类
class EquationFactory {
    public IEquation getEquation(String type) {
        if ("Add".equals(type)) {
            return new AddEquation();
        } else if ("Sub".equals(type)) {
            return new SubEquation();
        } else {
            throw new IllegalArgumentException("无效的方程类型: " + type);
        }
    }

    public IEquation getEquationRandom() {
        Random random = new Random();
        if (random.nextBoolean()) {
            return new AddEquation();
        } else {
            return new SubEquation();
        }
    }
}

// 定义算式产生类
class EquationCollection implements Iterable<IEquation> {
    private Set<IEquation> equations = new HashSet<>();

    public void generate(int n, EquationChecker checker) {
        EquationFactory factory = new EquationFactory();
        Random random = new Random();

        while (equations.size() < n) {
            IEquation equation = factory.getEquationRandom();
            equation.setOperand1((short) random.nextInt(101));
            equation.setOperand2((short) random.nextInt(101));
            equation.setOperator(random.nextBoolean() ? '+' : '-');

            if (checker.check(equation)) {
                equations.add(equation);
            }
        }
    }

    @Override
    public Iterator<IEquation> iterator() {
        return equations.iterator();
    }
}

// 定义Main类
class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("请输入生成算式的个数：");
        int n = scanner.nextInt();

        System.out.println("算数表达式为：");
        EquationCollection equationCollection = new EquationCollection();
        equationCollection.generate(n, new EquationCheckerOfRange(0, 100));

        for (IEquation equation : equationCollection) {
            System.out.println(equation.getOperand1() + " " + equation.getOperator() + " " +
                    equation.getOperand2() + " = " + equation.calculate());
        }
    }
}
