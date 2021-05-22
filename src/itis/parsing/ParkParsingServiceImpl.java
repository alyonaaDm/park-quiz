package itis.parsing;

        import itis.parsing.annotations.FieldName;
        import java.io.BufferedReader;
        import java.io.FileReader;
        import java.io.IOException;
        import java.lang.annotation.Annotation;
        import java.lang.reflect.Field;
        import java.lang.reflect.InvocationTargetException;

public class ParkParsingServiceImpl implements ParkParsingService {
    //Парсит файл в обьект класса "Park", либо бросает исключение с информацией об ошибках обработки
    @Override
    public Park parseParkData(String parkDatafilePath) throws ParkParsingException, IOException, IllegalAccessException {
        BufferedReader reader = new BufferedReader(new FileReader(parkDatafilePath));
        Park park = null;
        try {
            Class clas = Class.forName(Park.class.getName());
            Class[] parametrs = {};
            park = (Park) clas.getConstructor(parametrs).newInstance();
            clas.getDeclaredConstructor(parametrs).setAccessible(true);
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        Field[] fields = park.getClass().getDeclaredFields();
        String s;
        while ((s = reader.readLine()) != null) {
            if (!s.equals("***")) {
                String name = "";
                int k = 0;
                for (int i = 1; i < s.length(); i++) {
                    if (s.charAt(i) == '"') {
                        k = i;
                        break;
                    }
                    name += s.charAt(i);
                }
                String result = "";
                for (int i = k; i < s.length(); i++) {
                    if (s.charAt(i) != ':' && s.charAt(i) != '"' && s.charAt(i) != ' ') {
                        if (s.charAt(s.length() - 1) == '"') {
                            result = s.substring(i, s.length() - 1);
                        } else {
                            result = s.substring(i);
                        }
                    }
                }
                for (Field field : fields) {
                    field.setAccessible(true);
                    Annotation[] annotations = field.getAnnotations();
                    boolean checking = false;
                    FieldName annotation2 = field.getAnnotation(FieldName.class);
                    for (Annotation annotation : annotations) {
                        if (annotation.getClass().getName().equals(FieldName.class.getName())) {
                            checking = true;
                            break;
                        }
                    }
                    if(!checking){
                        if(field.getName().equals(name)){
                            field.set(park,result);
                        }
                    }else{
                        if(annotation2.value().equals(name)){
                            field.set(park,result);
                        }
                    }
                }
            }
        }
        return park;
    }
}
