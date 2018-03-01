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
 * MXExpressionManager.java
 * Copyright (C) 2018 University of Waikato, Hamilton, NZ
 */

package weka.core;

import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for managing expressions and applying them to datasets.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class MXExpressionBuilder
  implements Serializable {

  private static final long serialVersionUID = 9191069896843176421L;

  /** the expression to use. */
  protected String m_Expression;

  /** the range of attributes to consider. */
  protected Range m_Attributes;

  /** whether to use attribute names instead of attX. */
  protected boolean m_UseAttributeNames;

  /** the att index/argument name relation. */
  protected Map<Integer,String> m_ArgumentNames;

  /**
   * Initializes the manager.
   *
   * @param expression		the expression
   * @param attributes		the range of attributes to consider
   * @param useAttributeNames	whether to use attribute names or attX with X ranging from indices in attribute range
   */
  public MXExpressionBuilder(String expression, Range attributes, boolean useAttributeNames) {
    m_Expression        = expression;
    m_Attributes        = attributes;
    m_UseAttributeNames = useAttributeNames;
    m_ArgumentNames     = new HashMap<Integer, String>();
  }

  /**
   * Gets the expression in use.
   *
   * @return the expression
   */
  public String getExpression() {
    return m_Expression;
  }

  /**
   * Gets whether to use attribute names instead of indices ('attX').
   *
   * @return true if to use attribute names
   */
  public boolean getUseAttributeNames() {
    return m_UseAttributeNames;
  }

  /**
   * Gets the attribute range to consider.
   *
   * @return the attribute range
   */
  public Range getAttributes() {
    return m_Attributes;
  }

  /**
   * Builds the attribute name for the parser. Builds names only once and then
   * stores them in {@link #m_ArgumentNames}.
   *
   * @param inst	the current instance
   * @param index	the attribute index
   * @return		the name for the parser expression
   * @see		#m_ArgumentNames
   */
  protected String buildName(Instance inst, int index) {
    StringBuilder	result;
    String		name;
    int			i;
    char		c;

    if (!m_ArgumentNames.containsKey(index)) {
      if (getUseAttributeNames()) {
	result = new StringBuilder();
	name = inst.attribute(index).name().toLowerCase();
	for (i = 0; i < name.length(); i++) {
	  c = name.charAt(i);
	  if (Character.isLetterOrDigit(c))
	    result.append(c);
	}
	m_ArgumentNames.put(index, result.toString());
      }
      else {
	m_ArgumentNames.put(index, "att" + (index + 1));
      }
    }

    return m_ArgumentNames.get(index);
  }

  /**
   * Initializes the manager for the dataset.
   *
   * @param data	the dataset to initialize with
   */
  public void initialize(Instances data) {
    m_ArgumentNames.clear();
    m_Attributes.setUpper(data.numAttributes() - 1);
  }

  /**
   * Builds the expression using the provided instance.
   *
   * @param instance	the instance to use
   * @return		the generated expression
   */
  public Expression build(Instance instance) {
    List<Argument> 	args;
    Expression 		result;
    int			i;

    args = new ArrayList<Argument>();
    for (i = 0; i < instance.numAttributes(); i++) {
      if (instance.attribute(i).isNumeric() && m_Attributes.isInRange(i))
	args.add(new Argument(buildName(instance, i) + " = " + instance.value(i)));
    }
    result = new Expression(m_Expression, args.toArray(new Argument[args.size()]));

    return result;
  }
}
