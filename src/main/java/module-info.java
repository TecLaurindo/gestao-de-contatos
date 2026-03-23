module br.com.laurindoservicesrr.vini {
    requires javafx.controls;
    requires java.sql;
    requires org.apache.poi.ooxml;

    // Isso permite que o JavaFX acesse seus pacotes via Reflection
    opens br.com.laurindoservicesrr.vini to javafx.graphics;
    opens br.com.laurindoservicesrr.vini.model to javafx.base;
}