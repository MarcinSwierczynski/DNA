import com.structurizr.Workspace;
import com.structurizr.api.StructurizrClient;
import com.structurizr.documentation.Format;
import com.structurizr.documentation.StructurizrDocumentationTemplate;
import com.structurizr.model.*;
import com.structurizr.view.*;

/**
 * This is a simple example of how to get started with Structurizr for Java.
 */
public class Structurizr {

    private static final long WORKSPACE_ID = 46882;
    private static final String API_KEY = "c3b9d63a-5443-471e-abc6-2942b94094ac";
    private static final String API_SECRET = "d57c2bdb-d0da-43e4-aaad-c2dbb31cf0d1";
    private static final String EXISTING = "EXISTING";
    private static final String DATABASE = "database";

    public static void main(String[] args) throws Exception {
        // a Structurizr workspace is the wrapper for a software architecture model, views and documentation
        Workspace workspace = new Workspace("DNA training", "This is a model of my software system for DNA training.");
        Model model = workspace.getModel();

        // add some elements to your software architecture model
        Person user = model.addPerson("Content Manager", "Uploads XLSes with brochures.");

        SoftwareSystem parser = model.addSoftwareSystem("SBI Parser", "Allows to import static brochures using XLS files.");

        final SoftwareSystem orderInfoApi = model.addSoftwareSystem("Order Info API", "Allows to get information on orders");
        orderInfoApi.addTags(EXISTING);
        parser.uses(orderInfoApi, "Receive/sent data");

        Container apiApplication = parser.addContainer("API", "Provides REST API.", "Java i Spring MVC");
        Container database = parser.addContainer("Database", "Data on imported brochures", "PostgreSQL");
        database.addTags(DATABASE);
        user.uses(apiApplication, "Uses");
        apiApplication.uses(database, "Uses");

        model.addImplicitRelationships();

        // define some views (the diagrams you would like to see)
        ViewSet views = workspace.getViews();
        SystemContextView contextView = views.createSystemContextView(parser, "SystemContext", "An example of a System Context diagram.");
        contextView.addNearestNeighbours(parser);
        contextView.addAnimation(parser);
        contextView.addAnimation(user);
        contextView.addAnimation(orderInfoApi);


        //kontenery - C2
        ContainerView containerView = views.createContainerView(parser, "Containers", "Containers diagram");
        containerView.add(user);
        containerView.addAllContainers();
        containerView.add(parser);
        containerView.addAnimation(user, orderInfoApi);
        containerView.addAnimation(apiApplication);
        containerView.addAnimation(database);

        // add some documentation
        StructurizrDocumentationTemplate template = new StructurizrDocumentationTemplate(workspace);
        template.addContextSection(parser, Format.Markdown,
                "Here is some context about the software system...\n" +
                        "\n" +
                        "![](embed:SystemContext)");

        // add some styling
        Styles styles = views.getConfiguration().getStyles();
        styles.addElementStyle(Tags.SOFTWARE_SYSTEM).background("#1168bd").color("#ffffff");
        styles.addElementStyle(Tags.PERSON).background("#08427b").color("#ffffff").shape(Shape.Person);
        styles.addElementStyle(EXISTING).background("#898989").color("#ffffff");
        styles.addElementStyle(DATABASE).shape(Shape.Cylinder);

        uploadWorkspaceToStructurizr(workspace);
    }

    private static void uploadWorkspaceToStructurizr(Workspace workspace) throws Exception {
        StructurizrClient structurizrClient = new StructurizrClient(API_KEY, API_SECRET);
        structurizrClient.putWorkspace(WORKSPACE_ID, workspace);
    }

}