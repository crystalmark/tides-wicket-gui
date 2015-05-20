package uk.co.crystalmark.components;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.ILabelProvider;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.util.ListModel;

import java.util.List;

public abstract class BootstrapDropdownPanel<T> extends Panel implements ILabelProvider<T> {
    private static final long serialVersionUID = 1L;
    private IModel<T> model;
    private IModel<List<T>> availableList;
    private IChoiceRenderer<T> renderer;

    public BootstrapDropdownPanel(String id, IModel<T> model, IModel<List<T>> availableList) {
        super(id, model);
        this.model = model;
        this.availableList = availableList;
    }

    public BootstrapDropdownPanel(String id, IModel<T> model, List<T> availableList) {
        this(id, model, new ListModel(availableList));
    }

    protected void onInitialize() {
        super.onInitialize();
        LoadableDetachableModel labelModel = new LoadableDetachableModel() {
            private static final long serialVersionUID = 1L;

            protected String load() {
                return BootstrapDropdownPanel.this.model.getObject() == null ? "Please select" : (BootstrapDropdownPanel.this.renderer == null ? BootstrapDropdownPanel.this.model.getObject().toString() : BootstrapDropdownPanel.this.renderer.getDisplayValue(BootstrapDropdownPanel.this.model.getObject()) + "");
            }
        };
        this.add(new Component[]{new Label("triggerLabel", labelModel)});
        this.add(new Component[]{new ListView<T>("options", this.availableList) {
            private static final long serialVersionUID = 1L;

            protected void populateItem(final ListItem<T> item) {
                AjaxLink link = new AjaxLink("link") {
                    private static final long serialVersionUID = 1L;

                    public void onClick(AjaxRequestTarget target) {
                        BootstrapDropdownPanel.this.model.setObject(item.getModelObject());
                        BootstrapDropdownPanel.this.onChange(target, item.getModelObject());
                    }
                };
                item.add(new Component[]{link});
                LoadableDetachableModel labelModel = new LoadableDetachableModel() {
                    private static final long serialVersionUID = 1L;

                    protected String load() {
                        return item.getModelObject() == null ? null : (BootstrapDropdownPanel.this.renderer == null ? item.getModelObject().toString() : BootstrapDropdownPanel.this.renderer.getDisplayValue(item.getModelObject()) + "");
                    }
                };
                link.add(new Component[]{new Label("label", labelModel)});
            }
        }});
    }

    public BootstrapDropdownPanel<T> setRenderer(IChoiceRenderer<T> renderer) {
        this.renderer = renderer;
        return this;
    }

    public IModel<T> getLabel() {
        return this.model;
    }

    public abstract void onChange(AjaxRequestTarget var1, T var2);
}

