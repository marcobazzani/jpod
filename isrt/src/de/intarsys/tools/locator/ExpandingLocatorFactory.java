package de.intarsys.tools.locator;

import java.io.File;
import java.io.IOException;

import de.intarsys.tools.expression.EvaluationException;
import de.intarsys.tools.expression.IStringEvaluator;
import de.intarsys.tools.file.FileTools;
import de.intarsys.tools.functor.Args;

public class ExpandingLocatorFactory extends DelegatingLocatorFactory {

	final private IStringEvaluator evaluator;

	final private File parent;

	public ExpandingLocatorFactory(ILocatorFactory factory,
			IStringEvaluator evaluator, File parent) {
		super(factory);
		this.evaluator = evaluator;
		this.parent = parent;
	}

	@Override
	public ILocator createLocator(String location) throws IOException {
		String expanded;
		try {
			expanded = (String) evaluator.evaluate(location, Args.create());
		} catch (EvaluationException e) {
			expanded = location;
		}
		expanded = FileTools.trimPath(expanded);
		if (parent != null) {
			File output = FileTools.resolvePath(parent, expanded);
			return super.createLocator(output.getAbsolutePath());
		}
		return super.createLocator(expanded);
	}

}
