package chord.relations;

import org.junit.platform.suite.api.ExcludePackages;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@SelectPackages({"chord.relations"})
@ExcludePackages({"chord.relations.persist",
					"chord.relations.record",
					"chord.relations.request"})
@Suite
public class RelationsTestSuite {
}
