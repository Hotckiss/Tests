import org.easymock.Capture;
import org.easymock.EasyMock;
import org.easymock.EasyMockRunner;
import org.easymock.EasyMockSupport;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

public class EasyMockExamples {

    /*
    План:
    1) создание мока и его активация
    2) порядок вызова методов (strict mock)
    3) дефолтная реализация (nice mock)
    4) verify -- проверка использования мока
    5) регулируем количество вызовов
    6) кидание исключение моком
    7) частичный мок
    8) мокаем void метод
    9) arg matchers, capture
    10) annotations

    static, final, private -- not supported, нужен PowerMock
     */
    @Test
    //простой пример. создание мока. простейший метод для тестирования
    public void testAdd(){
        //set up
        MathApplication mathApplication = new MathApplication();

        //создание мока. Данный метод создает мок-объект, в котором порядок вызова методов значения не имеет
        CalculatorService calcService = EasyMock.createMock(CalculatorService.class);
        mathApplication.setCalculatorService(calcService);

        //добавляем поведение объекту
        EasyMock.expect(calcService.add(10.0,20.0)).andReturn(30.00);

        //переводим мок-объект из состояния записи данных в состояние их воспроизведения
        EasyMock.replay(calcService);

        //тестируем, после replay метод можно вызывать
        Assert.assertEquals(mathApplication.add(10.0, 20.0),30.0,0);
    }

    @Test
    //Порядок вызова объектов не важен
    public void testAdd1(){
        //set up
        MathApplication mathApplication = new MathApplication();

        //создание мока. Данный метод создает мок-объект, в котором порядок вызова методов значения не имеет
        CalculatorService calcService = EasyMock.createMock(CalculatorService.class);
        mathApplication.setCalculatorService(calcService);

        //добавляем поведение объекту
        EasyMock.expect(calcService.add(10.0,20.0)).andReturn(30.00);
        EasyMock.expect(calcService.subtract(20.0, 10.0)).andReturn(10.00);

        //переводим мок-объект из состояния записи данных в состояние их воспроизведения
        EasyMock.replay(calcService);

        //тестируем, после replay метод можно вызывать
        Assert.assertEquals(mathApplication.add(10.0, 20.0),30.0,0);
        Assert.assertEquals(mathApplication.subtract(20.0, 10.0),10.0,0);
    }

    @Test
    //Порядок вызова объектов не важен
    public void testAdd2(){
        //set up
        MathApplication mathApplication = new MathApplication();

        //создание мока. Данный метод создает мок-объект, в котором порядок вызова методов значения не имеет
        CalculatorService calcService = EasyMock.createMock(CalculatorService.class);
        mathApplication.setCalculatorService(calcService);

        //добавляем поведение объекту
        EasyMock.expect(calcService.add(10.0,20.0)).andReturn(30.00);
        EasyMock.expect(calcService.subtract(20.0, 10.0)).andReturn(10.00);

        //переводим мок-объект из состояния записи данных в состояние их воспроизведения
        EasyMock.replay(calcService);

        //тестируем, после replay метод можно вызывать
        Assert.assertEquals(mathApplication.subtract(20.0, 10.0),10.0,0);
        Assert.assertEquals(mathApplication.add(10.0, 20.0),30.0,0);
    }

    @Test
    //Порядок вызова объектов ВАЖЕН
    public void testAdd3(){
        //set up
        MathApplication mathApplication = new MathApplication();

        // strict mock
        CalculatorService calcService = EasyMock.createStrictMock(CalculatorService.class);
        mathApplication.setCalculatorService(calcService);

        //добавляем поведение объекту
        EasyMock.expect(calcService.add(10.0,20.0)).andReturn(30.00);
        EasyMock.expect(calcService.subtract(20.0, 10.0)).andReturn(10.00);

        //переводим мок-объект из состояния записи данных в состояние их воспроизведения
        EasyMock.replay(calcService);

        //тестируем, после replay метод можно вызывать
        Assert.assertEquals(mathApplication.subtract(20.0, 10.0),10.0,0);
        Assert.assertEquals(mathApplication.add(10.0, 20.0),30.0,0);
    }

    @Test
    //Порядок вызова объектов ВАЖЕН
    public void testAdd4(){
        //set up
        MathApplication mathApplication = new MathApplication();

        // strict mock
        CalculatorService calcService = EasyMock.createStrictMock(CalculatorService.class);
        mathApplication.setCalculatorService(calcService);

        //добавляем поведение объекту
        EasyMock.expect(calcService.add(10.0,20.0)).andReturn(30.00);
        EasyMock.expect(calcService.subtract(20.0, 10.0)).andReturn(10.00);

        //переводим мок-объект из состояния записи данных в состояние их воспроизведения
        EasyMock.replay(calcService);

        //тестируем, после replay метод можно вызывать
        Assert.assertEquals(mathApplication.add(10.0, 20.0),30.0,0);
        Assert.assertEquals(mathApplication.subtract(20.0, 10.0),10.0,0);
    }

    @Test
    //Проблема: вызов метода, который не замокан. Получаем исключение
    public void testAdd5(){
        //set up
        MathApplication mathApplication = new MathApplication();

        CalculatorService calcService = EasyMock.createMock(CalculatorService.class);
        mathApplication.setCalculatorService(calcService);

        //добавляем поведение объекту
        EasyMock.expect(calcService.add(10.0,20.0)).andReturn(30.00);

        //переводим мок-объект из состояния записи данных в состояние их воспроизведения
        EasyMock.replay(calcService);

        //тестируем, после replay метод можно вызывать
        Assert.assertEquals(mathApplication.subtract(20.0, 10.0),10.0,0);
    }

    @Test
    // Nice mock, возвращает дефолтные значения -- 0, null, false
    public void testAdd6(){
        //set up
        MathApplication mathApplication = new MathApplication();

        // nice mock
        CalculatorService calcService = EasyMock.createNiceMock(CalculatorService.class);
        mathApplication.setCalculatorService(calcService);

        //добавляем поведение объекту
        EasyMock.expect(calcService.add(10.0,20.0)).andReturn(30.00);

        //переводим мок-объект из состояния записи данных в состояние их воспроизведения
        EasyMock.replay(calcService);

        //тестируем, после replay метод можно вызывать
        Assert.assertEquals(mathApplication.subtract(20.0, 10.0),0.0,0);
    }

    @Test
    // Теперь хочется вообще проверять, что метод мока вызывался
    // точнее -- что просто использовали объект
    //тест пройдет. но мок не использовался
    public void testAdd7(){
        //set up
        //выполняет операцию напрямую. не использует замоканный сервис
        MathApplicationImpl mathApplication = new MathApplicationImpl();

        CalculatorService calcService = EasyMock.createMock(CalculatorService.class);
        mathApplication.setCalculatorService(calcService);

        //добавляем поведение объекту
        EasyMock.expect(calcService.add(10.0,20.0)).andReturn(30.00);

        //переводим мок-объект из состояния записи данных в состояние их воспроизведения
        EasyMock.replay(calcService);

        //тестируем, после replay метод можно вызывать
        Assert.assertEquals(mathApplication.add(10.0, 20.0),30.0,0);
    }

    @Test
    // verify -- тест не пройдет, так как не использовали мок
    public void testAdd8(){
        //set up
        //выполняет операцию напрямую. не использует замоканный сервис
        MathApplicationImpl mathApplication = new MathApplicationImpl();

        CalculatorService calcService = EasyMock.createMock(CalculatorService.class);
        mathApplication.setCalculatorService(calcService);

        //добавляем поведение объекту
        EasyMock.expect(calcService.add(10.0,20.0)).andReturn(30.00);

        //переводим мок-объект из состояния записи данных в состояние их воспроизведения
        EasyMock.replay(calcService);

        //тестируем, после replay метод можно вызывать
        Assert.assertEquals(mathApplication.add(10.0, 20.0),30.0,0);

        EasyMock.verify(calcService);
    }

    @Test
    // verify -- OK. используем сервис
    public void testAdd9(){
        //set up
        //выполняет операцию напрямую. не использует замоканный сервис
        MathApplication mathApplication = new MathApplication();

        CalculatorService calcService = EasyMock.createMock(CalculatorService.class);
        mathApplication.setCalculatorService(calcService);

        //добавляем поведение объекту
        EasyMock.expect(calcService.add(10.0,20.0)).andReturn(30.00);

        //переводим мок-объект из состояния записи данных в состояние их воспроизведения
        EasyMock.replay(calcService);

        //тестируем, после replay метод можно вызывать
        Assert.assertEquals(mathApplication.add(10.0, 20.0),30.0,0);

        //есть также verifyAll и replayAll
        EasyMock.verify(calcService);
    }

    @Test
    // можно задвать количество вызовов методов, оно проверится в verify
    public void testAdd10(){
        MathApplication mathApplication = new MathApplication();

        // strict mock
        CalculatorService calcService = EasyMock.createMock(CalculatorService.class);
        mathApplication.setCalculatorService(calcService);

        //5 раз
        EasyMock.expect(calcService.add(10.0,20.0)).andReturn(30.00).times(5);

        //1 раз
        EasyMock.expect(calcService.multiply(10.0,20.0)).andReturn(200.00).once();

        //1+
        EasyMock.expect(calcService.add(10.0,200.0)).andReturn(210.00).atLeastOnce();

        //любое число раз
        EasyMock.expect(calcService.add(100.0,20.0)).andReturn(120.00).anyTimes();

        //1-3 раза
        EasyMock.expect(calcService.add(1000.0,20.0)).andReturn(1020.00).times(1, 3);

        EasyMock.replay(calcService);
        Assert.assertEquals(mathApplication.add(10.0, 20.0),30.0,0);

        EasyMock.verify(calcService);
    }

    @Test
    // можно задвать количество вызовов методов, оно проверится в verify
    public void testAdd11(){
        MathApplication mathApplication = new MathApplication();

        // strict mock
        CalculatorService calcService = EasyMock.createMock(CalculatorService.class);
        mathApplication.setCalculatorService(calcService);

        //5 раз
        EasyMock.expect(calcService.add(10.0,20.0)).andReturn(30.00).times(5);

        //1 раз
        EasyMock.expect(calcService.multiply(10.0,20.0)).andReturn(200.00).once();

        //1+
        EasyMock.expect(calcService.add(10.0,200.0)).andReturn(210.00).atLeastOnce();

        //любое число раз
        EasyMock.expect(calcService.add(100.0,20.0)).andReturn(120.00).anyTimes();

        //1-3 раза
        EasyMock.expect(calcService.add(1000.0,20.0)).andReturn(1020.00).times(1, 3);

        EasyMock.replay(calcService);

        Assert.assertEquals(mathApplication.add(10.0, 20.0),30.0,0);
        Assert.assertEquals(mathApplication.add(10.0, 20.0),30.0,0);
        Assert.assertEquals(mathApplication.add(10.0, 20.0),30.0,0);
        Assert.assertEquals(mathApplication.add(10.0, 20.0),30.0,0);
        Assert.assertEquals(mathApplication.add(10.0, 20.0),30.0,0);

        Assert.assertEquals(mathApplication.multiply(10.0, 20.0),200.0,0);

        Assert.assertEquals(mathApplication.add(10.0, 200.0),210.0,0);
        Assert.assertEquals(mathApplication.add(10.0, 200.0),210.0,0);
        Assert.assertEquals(mathApplication.add(10.0, 200.0),210.0,0);
        Assert.assertEquals(mathApplication.add(10.0, 200.0),210.0,0);
        Assert.assertEquals(mathApplication.add(10.0, 200.0),210.0,0);
        Assert.assertEquals(mathApplication.add(10.0, 200.0),210.0,0);
        Assert.assertEquals(mathApplication.add(10.0, 200.0),210.0,0);
        Assert.assertEquals(mathApplication.add(10.0, 200.0),210.0,0);
        Assert.assertEquals(mathApplication.add(10.0, 200.0),210.0,0);

        //так как можно 0 раз
        //calcService.add(100.0,20.0);

        // 1-3 раза
        Assert.assertEquals(mathApplication.add(1000.0, 20.0),1020.0,0);
        Assert.assertEquals(mathApplication.add(1000.0, 20.0),1020.0,0);

        EasyMock.verify(calcService);
    }

    @Test(expected = RuntimeException.class)
    // добавляем в поведение бросание исключений
    public void testAdd12(){
        MathApplication mathApplication = new MathApplication();

        CalculatorService calcService = EasyMock.createMock(CalculatorService.class);
        mathApplication.setCalculatorService(calcService);

        //добавляем поведение объекту
        EasyMock.expect(calcService.add(10.0,20.0)).andThrow(new
                RuntimeException("Add operation not implemented"));;

        EasyMock.replay(calcService);

        // тут полетело исключение
        Assert.assertEquals(mathApplication.add(10.0, 20.0),30.0,0);

        EasyMock.verify(calcService);
    }

    @Test
    // можно замокать часть методов, например вск кроме сложения
    // для интерфейса не испеет смысл и кидает исключение
    public void testAdd13() {

        MathApplicationImpl mathApplication = EasyMock.partialMockBuilder(MathApplicationImpl.class)
                .addMockedMethod("subtract")
                .addMockedMethod("multiply")
                .addMockedMethod("divide")
                .createMock();


        EasyMock.expect(mathApplication.multiply(10.0, 20.0)).andReturn(200.00);

        EasyMock.replay(mathApplication);

        Assert.assertEquals(mathApplication.multiply(10.0, 20.0), 200.0, 0);

        //OK
        Assert.assertEquals(mathApplication.add(10.0, 20.0), 30.0, 0);

        EasyMock.verify(mathApplication);
    }

    @Test
    // хотим замокать void метод, вместо expect юзаем expectLastCall
    public void testAdd14() {
        StringUtils mock = EasyMock.mock(StringUtils.class);

        mock.print(EasyMock.anyString());
        // andVoid() -- можно не делать ничего
        EasyMock.expectLastCall().andAnswer(() -> {
            System.out.println("Mock Argument = "
                    + EasyMock.getCurrentArguments()[0]);
            return null;
        }).times(2);
        EasyMock.replay(mock);

        mock.print("Java");
        mock.print("Python");
        EasyMock.verify(mock);
    }

    @Test
    // argument matchers -- не конкретизируем аргументы
    // для строк есть матчеры по типу endsWith. есть регулярные выражения
    public void testAdd15() {
        double big = 1000.0;
        double inf = 100000.0;

        MathApplication mathApplication = new MathApplication();

        CalculatorService calcService = EasyMock.createMock(CalculatorService.class);
        mathApplication.setCalculatorService(calcService);

        //добавляем поведение объекту
        EasyMock.expect(calcService.add(EasyMock.geq(big), EasyMock.geq(big))).andReturn(inf);

        //переводим мок-объект из состояния записи данных в состояние их воспроизведения
        EasyMock.replay(calcService);

        Assert.assertEquals(mathApplication.add(1000.0, 1001.0),inf,0);

        EasyMock.verify(calcService);
    }

    @Test
    // arg matchers
    public void testAdd16() {
        StringUtils mock = EasyMock.mock(StringUtils.class);

        EasyMock.expect(mock.len(EasyMock.notNull())).andReturn(5);
        EasyMock.expect(mock.len(null)).andThrow(new RuntimeException()).anyTimes();
        EasyMock.replay(mock);

        Assert.assertEquals(5, mock.len("aaa"));
        EasyMock.verify(mock);
    }

    @Test(expected = RuntimeException.class)
    // arg matchers
    public void testAdd17() {
        StringUtils mock = EasyMock.mock(StringUtils.class);

        EasyMock.expect(mock.len(EasyMock.notNull())).andReturn(5);
        EasyMock.expect(mock.len(null)).andThrow(new RuntimeException()).anyTimes();
        EasyMock.replay(mock);

        Assert.assertEquals(5, mock.len(null));
        EasyMock.verify(mock);
    }

    @Test
    // используем матчеры, хотим узнать что в итоге подставилось
    public void testAdd18() {
        ArrayList<String> mockList = EasyMock.mock(ArrayList.class);
        //create capture
        Capture<String> captureSingleArgument = EasyMock.newCapture();
        //add capture to list
        EasyMock.expect(mockList.add(EasyMock.capture(captureSingleArgument))).andReturn(true);
        EasyMock.replay(mockList);

        Assert.assertTrue(mockList.add("Hello"));

        System.out.println(captureSingleArgument.getValue());

        EasyMock.verify(mockList);
    }
}