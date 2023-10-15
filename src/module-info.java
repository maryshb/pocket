module pocketDesktop {
  requires javafx.fxml;
  requires javafx.controls;
  requires javafx.base;
  requires javafx.graphics;
  requires lombok;
  requires spring.boot.starter;
  requires spring.context;
  requires spring.boot.autoconfigure;
  requires spring.boot;
  requires spring.web;
  requires spring.beans;
  requires spring.core;
  requires JxBrowser;
  requires jxbrowser.javafx;
  requires org.controlsfx.controls;
  requires de.jensd.fx.glyphs.materialdesignicons;
  requires com.fasterxml.jackson.databind;
  requires com.fasterxml.jackson.core;
  requires org.apache.commons.io;
  requires org.slf4j;
  opens pl.wszib.edu.pocketdesktop to javafx.graphics;
  exports pl.wszib.edu.pocketdesktop;
}

