package live.cottons;

import org.springframework.ui.Model;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public interface TemplateGenerator {
    default Map<String, Object> generate() {
        Map<String, Object> map = new HashMap<>();
        final String className = this.getClass().getSimpleName();
        final ArrayList<String> ignoreFields = ignoreFields();

       try{
           for (Field f : this.getClass().getDeclaredFields()) {
               if (ignoreFields.contains(f.getName())) {
                   continue;
               }
               f.setAccessible(true);
               map.put(f.getName(), f.get(this));
           }
       }catch(Exception e){
           System.err.println("Error at " + className + ": " + e.getMessage());
       }
        return map;
    }

    default ArrayList<String> ignoreFields() {
        return new ArrayList<>();
    }

    default void buildModel(Model model) {
        final var map = this.generate();
        for (var entry : map.entrySet()) {
            model.addAttribute(entry.getKey(), entry.getValue());
        }
    }
}
