package modelanalyzer.examples;

import org.eclipse.emf.ecore.EObject;
import org.junit.Test;

import eu.mondo.map.modeladapters.emf.EmfModelAdapter;
import eu.mondo.map.modelanalyzer.ModelAnalyzer;
import eu.mondo.map.modelmetrics.ModelMetric;
import eu.mondo.map.modelmetrics.impl.ModelMetrics;
import eu.mondo.map.modelmetrics.impl.simple.NumberOfEdges;
import eu.mondo.map.modelmetrics.impl.simple.NumberOfNodes;
import eu.mondo.map.tests.model.emf.network.NetworkFactory;
import eu.mondo.map.tests.model.emf.network.Node;
import eu.mondo.map.tests.model.emf.network.NodeContainer;

public class Examples {

    @Test
    public void useConcreteMetric() {
	System.out.println("Example for using concrete metric");
	// init metrics
	NumberOfNodes numberOfNodes = new NumberOfNodes();
	NumberOfEdges numberOfEdges = new NumberOfEdges();

	// init an example model, it could be any arbitrary emf model
	EObject container = initModel();

	// the adapter represents a bridge between model and metrics
	EmfModelAdapter adapter = new EmfModelAdapter();
	// adapter must be initialized by a container node, this will creates
	// the index which is necessary during the evaluation
	adapter.init(container);

	// calculate metrics
	numberOfNodes.evaluate(adapter);
	numberOfEdges.evaluate(adapter);

	System.out.println("\nResults:");

	// get the results
	showResult(numberOfNodes);
	showResult(numberOfEdges);
    }

    @Test
    public void useTheAnalyzer() {
	System.out.println("Example for using the model analyzer");
	ModelAnalyzer analyzer = new ModelAnalyzer();

	// use the ModelMetrics enum instead of referring to the concrete
	// implementation
	analyzer.use(ModelMetrics.NumberOfNodes).use(ModelMetrics.NumberOfEdges);

	EObject container = initModel();
	EmfModelAdapter adapter = new EmfModelAdapter();
	adapter.init(container);

	analyzer.evaluate(adapter);

	System.out.println("\nResults:");
	showResult(analyzer.getMetric(ModelMetrics.NumberOfNodes));
	showResult(analyzer.getMetric(ModelMetrics.NumberOfEdges));
    }

    @Test
    public void evaluateAll() {
	System.out.println("Example for using the model analyzer for each metrics");
	EObject container = initModel();
	EmfModelAdapter adapter = new EmfModelAdapter();
	adapter.init(container);

	ModelAnalyzer analyzer = new ModelAnalyzer();
	analyzer.useAll().evaluate(adapter);

	// show all
	System.out.println("\nResults:");
	analyzer.getMetricsInOrder().forEach(metric -> {
	    showResult(metric);
	});
    }

    private NodeContainer initModel() {
	NetworkFactory factory = NetworkFactory.eINSTANCE;
	NodeContainer container = factory.createNodeContainer();

	// create the network of 2 nodes and 1 edge
	Node source = factory.createNode();
	Node target = factory.createNode();
	source.getDim1().add(target);

	container.getNodes().add(source);
	return container;
    }

    private void showResult(ModelMetric metric) {
	System.out.println(String.format("Data of %s : %s", metric.getName(), metric.getData().toString()));
    }

}
