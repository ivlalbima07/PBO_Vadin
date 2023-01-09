package com.example.application.views.subcategory;

import com.example.application.data.entity.Subcategory;
import com.example.application.data.service.SubcategoryService;
import com.example.application.views.MainLayout;
import com.example.application.views.category.CategoryView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import java.util.Optional;
import org.springframework.data.domain.PageRequest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

@PageTitle("Subcategory")
@Route(value = "subcategory/:subcategoryID?/:action?(edit)", layout = MainLayout.class)
public class SubcategoryView extends Div implements BeforeEnterObserver {

    private final String SUBCATEGORY_ID = "subcategoryID";
    private final String SUBCATEGORY_EDIT_ROUTE_TEMPLATE = "subcategory/%s/edit";

    private final Grid<Subcategory> grid = new Grid<>(Subcategory.class, false);

    private DatePicker tanggal;
    private TextField subcategory;

    private final Button cancel = new Button("Cancel");
    private final Button save = new Button("Save");
    private final Button delete = new Button("delete");

    private final BeanValidationBinder<Subcategory> binder;

    private Subcategory subcategories;

    private final SubcategoryService subcategoryService;

    public SubcategoryView(SubcategoryService subcategoryService) {
        this.subcategoryService = subcategoryService;
        addClassNames("subcategory-view");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("tanggal").setAutoWidth(true);
        grid.addColumn("subcategory").setAutoWidth(true);
        grid.setItems(query -> subcategoryService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(SUBCATEGORY_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(SubcategoryView.class);
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(Subcategory.class);

        // Bind fields. This is where you'd define e.g. validation rules

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        delete.addClickListener(e -> {
            try {
                if (this.subcategory == null) {
                    Notification.show("no sampleperson selected");
                }else {
                    binder.writeBean(this.subcategories);
                    subcategoryService.delete(this.subcategories.getId());
                    clearForm();
                    refreshGrid();
                    Notification.show("sampleperson details stored");
                    UI.getCurrent().navigate(SubcategoryView.class);
                }
            } catch (ValidationException validationException){
                Notification.show(" An exception happened while trying to store the sampleperson details.");
            }
        });

        save.addClickListener(e -> {
            try {
                if (this.subcategories == null) {
                    this.subcategories = new Subcategory();
                }
                binder.writeBean(this.subcategories);
                subcategoryService.update(this.subcategories);
                clearForm();
                refreshGrid();
                Notification.show("Data updated");
                UI.getCurrent().navigate(SubcategoryView.class);
            } catch (ObjectOptimisticLockingFailureException exception) {
                Notification n = Notification.show(
                        "Error updating the data. Somebody else has updated the record while you were making changes.");
                n.setPosition(Position.MIDDLE);
                n.addThemeVariants(NotificationVariant.LUMO_ERROR);
            } catch (ValidationException validationException) {
                Notification.show("Failed to update the data. Check again that all values are valid");
            }
        });
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<Long> subcategoryId = event.getRouteParameters().get(SUBCATEGORY_ID).map(Long::parseLong);
        if (subcategoryId.isPresent()) {
            Optional<Subcategory> subcategoryFromBackend = subcategoryService.get(subcategoryId.get());
            if (subcategoryFromBackend.isPresent()) {
                populateForm(subcategoryFromBackend.get());
            } else {
                Notification.show(
                        String.format("The requested subcategory was not found, ID = %s", subcategoryId.get()), 3000,
                        Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(SubcategoryView.class);
            }
        }
    }

    private void createEditorLayout(SplitLayout splitLayout) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setClassName("editor-layout");

        Div editorDiv = new Div();
        editorDiv.setClassName("editor");
        editorLayoutDiv.add(editorDiv);

        FormLayout formLayout = new FormLayout();
        tanggal = new DatePicker("Tanggal");
        subcategory = new TextField("Subcategory");
        formLayout.add(tanggal, subcategory);

        editorDiv.add(formLayout);
        createButtonLayout(editorLayoutDiv);

        splitLayout.addToSecondary(editorLayoutDiv);
    }

    private void createButtonLayout(Div editorLayoutDiv) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setClassName("button-layout");
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        buttonLayout.add(save, delete, cancel);
        editorLayoutDiv.add(buttonLayout);
    }

    private void createGridLayout(SplitLayout splitLayout) {
        Div wrapper = new Div();
        wrapper.setClassName("grid-wrapper");
        splitLayout.addToPrimary(wrapper);
        wrapper.add(grid);
    }

    private void refreshGrid() {
        grid.select(null);
        grid.getDataProvider().refreshAll();
    }

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(Subcategory value) {
        this.subcategories = value;
        binder.readBean(this.subcategories);

    }
}
