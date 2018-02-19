/*
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/*
 * Copyright (C) 2002-2016 University of Waikato
 */

package weka.filters.unsupervised.attribute;

import junit.framework.Test;
import junit.framework.TestSuite;
import weka.classifiers.functions.LinearRegression;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.Attribute;
import weka.core.Instances;
import weka.core.TestInstances;
import weka.filters.AbstractFilterTest;
import weka.filters.Filter;

/**
 * Tests MXExpression. Run from the command line with:
 * <pre>
 * java weka.filters.unsupervised.attribute.MXExpressionTest
 * </pre>
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class MXExpressionTest
  extends AbstractFilterTest {

  /**
   * Initializes the test.
   *
   * @param name 	the name of the test
   */
  public MXExpressionTest(String name) {
    super(name);
    System.setProperty("weka.test.Regression.root", "src/test/resources");
  }

  /**
   * Creates a default filter.
   * 
   * @return		the filter
   */
  @Override
  public Filter getFilter() {
    return new MXExpression();
  }

  /**
   * Called by JUnit before each test method. This implementation creates
   * the default filter to test and loads a test set of Instances.
   *
   * @throws Exception if an error occurs reading the example instances.
   */
  @Override
  protected void setUp() throws Exception {
    super.setUp();
    m_Instances = getFilteredClassifierData();
  }

  /**
   * returns data generated for the FilteredClassifier test
   *
   * @return		the dataset for the FilteredClassifier
   * @throws Exception	if generation of data fails
   */
  @Override
  protected Instances getFilteredClassifierData() throws Exception {
    TestInstances data = new TestInstances();
    data.setClassType(Attribute.NUMERIC);
    data.setClassIndex(TestInstances.CLASS_IS_LAST);
    data.setNumInstances(20);
    data.setNumNumeric(10);
    data.setNumNominal(0);
    data.setNumDate(1);
    return data.generate();
  }

  /**
   * returns the configured FilteredClassifier. Since the base classifier is
   * determined heuristically, derived tests might need to adjust it.
   *
   * @return the configured FilteredClassifier
   */
  @Override
  protected FilteredClassifier getFilteredClassifier() {
    FilteredClassifier	result;

    result = new FilteredClassifier();
    result.setFilter(getFilter());
    result.setClassifier(new LinearRegression());

    return result;
  }

  /**
   * Tests the default setup.
   */
  public void testTypical() {
    Instances result = useFilter();
    assertEquals(m_Instances.numAttributes(), result.numAttributes());
    assertEquals(m_Instances.numInstances(), result.numInstances());
  }

  /**
   * Tests attribute indices.
   */
  public void testAttributeIndices() {
    MXExpression filter = new MXExpression();
    filter.setExpression("(att1 + att3) / att5");
    filter.setUseAttributeNames(false);
    m_Filter = filter;
    Instances result = useFilter();
    assertEquals(m_Instances.numAttributes(), result.numAttributes());
    assertEquals(m_Instances.numInstances(), result.numInstances());
  }

  /**
   * Tests attribute names.
   */
  public void testAttributeNames() {
    MXExpression filter = new MXExpression();
    filter.setExpression("(numeric1 + numeric3) / numeric5");
    filter.setUseAttributeNames(true);
    m_Filter = filter;
    Instances result = useFilter();
    assertEquals(m_Instances.numAttributes(), result.numAttributes());
    assertEquals(m_Instances.numInstances(), result.numInstances());
  }

  /**
   * Creates a test suite.
   * 
   * @return		the suite
   */
  public static Test suite() {
    return new TestSuite(MXExpressionTest.class);
  }

  /**
   * Executes the test from commandline.
   * 
   * @param args	ignored
   */
  public static void main(String[] args){
    junit.textui.TestRunner.run(suite());
  }
}
