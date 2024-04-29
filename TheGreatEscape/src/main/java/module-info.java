module org.game.thegreatescape {
  requires javafx.controls;
  requires com.fasterxml.jackson.databind;
    requires org.json;
    requires java.logging;

    exports org.game.thegreatescape.levelEditor;

  opens org.game.thegreatescape.levelEditor to com.fasterxml.jackson.databind;
  exports org.game.thegreatescape.view;
  opens org.game.thegreatescape.view to com.fasterxml.jackson.databind;
    exports org.game.thegreatescape.model;
    opens org.game.thegreatescape.model to com.fasterxml.jackson.databind;
}