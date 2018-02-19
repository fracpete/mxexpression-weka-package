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
 * MXExpression.java
 * Copyright (C) 2018 University of Waikato, Hamilton, NZ
 */

package weka.classifiers.functions;

import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;
import org.mariuszgromada.math.mxparser.mXparser;
import weka.classifiers.AbstractClassifier;
import weka.core.Capabilities;
import weka.core.Capabilities.Capability;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Option;
import weka.core.RevisionUtils;
import weka.core.Utils;
import weka.core.WekaException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 <!-- globalinfo-start -->
 * Applies the specified expression to make a prediction.<br>
 * All numeric attribute values are accessible via 'attX' (with X being  the 1-based index of the attribute) or their attribute name (lower case, all non-alphanumeric characters removed).<br>
 * <br>
 * Expression help:<br>
 * Help content: <br>
 * <br>
 *     #  key word            type                    syntax                                        since description<br>
 *     -  --------            ----                    ------                                        ----- -----------<br>
 *     1. _number_            &lt;number&gt;                1, -2, 001, +001.2e-10, b1.111, b2.1001, b3.12021, b16.af12, ...1.0   Regullar expression for decimal numbers<br>
 *     2. +                   &lt;Operator&gt;              a + b                                         1.0   Addition<br>
 *     3. -                   &lt;Operator&gt;              a - b                                         1.0   Subtraction<br>
 *     4. *                   &lt;Operator&gt;              a * b                                         1.0   Nultiplication<br>
 *     5. /                   &lt;Operator&gt;              a / b                                         1.0   Division<br>
 *     6. ^                   &lt;Operator&gt;              a^b                                           1.0   Exponentiation<br>
 *     7. !                   &lt;Operator&gt;              n!                                            1.0   Factorial<br>
 *     8. #                   &lt;Operator&gt;              a # b                                         1.0   Modulo function<br>
 *     9. %                   &lt;Operator&gt;              n%                                            4.1   Percentage<br>
 *    10. &amp;                   &lt;Boolean Operator&gt;      p &amp; q                                         1.0   Logical conjunction (AND)<br>
 *    11. &amp;&amp;                  &lt;Boolean Operator&gt;      p &amp;&amp; q                                        1.0   Logical conjunction (AND)<br>
 *    12. /\                  &lt;Boolean Operator&gt;      p /\ q                                        1.0   Logical conjunction (AND)<br>
 *    13. ~&amp;                  &lt;Boolean Operator&gt;      p ~&amp; q                                        1.0   NAND - Sheffer stroke<br>
 *    14. ~&amp;&amp;                 &lt;Boolean Operator&gt;      p ~&amp;&amp; q                                       1.0   NAND - Sheffer stroke<br>
 *    15. ~/\                 &lt;Boolean Operator&gt;      p ~/\ q                                       1.0   NAND - Sheffer stroke<br>
 *    16. |                   &lt;Boolean Operator&gt;      p | q                                         1.0   Logical disjunction (OR)<br>
 *    17. ||                  &lt;Boolean Operator&gt;      p || q                                        1.0   Logical disjunction (OR)<br>
 *    18. \/                  &lt;Boolean Operator&gt;      p \/ q                                        1.0   Logical disjunction (OR)<br>
 *    19. ~|                  &lt;Boolean Operator&gt;      p ~| q                                        1.0   Logical NOR<br>
 *    20. ~||                 &lt;Boolean Operator&gt;      p ~|| q                                       1.0   Logical NOR<br>
 *    21. ~\/                 &lt;Boolean Operator&gt;      p ~\/ q                                       1.0   Logical NOR<br>
 *    22. (+)                 &lt;Boolean Operator&gt;      p (+) q                                       1.0   Exclusive or (XOR)<br>
 *    23. --&gt;                 &lt;Boolean Operator&gt;      p --&gt; q                                       1.0   Implication (IMP)<br>
 *    24. &lt;--                 &lt;Boolean Operator&gt;      p &lt;-- q                                       1.0   Converse implication (CIMP)<br>
 *    25. -/&gt;                 &lt;Boolean Operator&gt;      p  -/&gt; q                                      1.0   Material nonimplication (NIMP)<br>
 *    26. &lt;/-                 &lt;Boolean Operator&gt;      p &lt;/- q                                       1.0   Converse nonimplication (CNIMP)<br>
 *    27. &lt;-&gt;                 &lt;Boolean Operator&gt;      p &lt;-&gt; q                                       1.0   Logical biconditional (EQV)<br>
 *    28. ~                   &lt;Boolean Operator&gt;      ~p                                            1.0   Negation<br>
 *    29. =                   &lt;Binary Relation&gt;       a = b                                         1.0   Equality<br>
 *    30. ==                  &lt;Binary Relation&gt;       a == b                                        1.0   Equality<br>
 *    31. &lt;&gt;                  &lt;Binary Relation&gt;       a &lt;&gt; b                                        1.0   Inequation<br>
 *    32. ~=                  &lt;Binary Relation&gt;       a ~= b                                        1.0   Inequation<br>
 *    33. !=                  &lt;Binary Relation&gt;       a != b                                        1.0   Inequation<br>
 *    34. &lt;                   &lt;Binary Relation&gt;       a &lt; b                                         1.0   Lower than<br>
 *    35. &gt;                   &lt;Binary Relation&gt;       a &gt; b                                         1.0   Greater than<br>
 *    36. &lt;=                  &lt;Binary Relation&gt;       a &lt;= b                                        1.0   Lower or equal<br>
 *    37. &gt;=                  &lt;Binary Relation&gt;       a &gt;= b                                        1.0   Greater or equal<br>
 *    38. sin                 &lt;Unary Function&gt;        sin(x)                                        1.0   Trigonometric sine function<br>
 *    39. cos                 &lt;Unary Function&gt;        cos(x)                                        1.0   Trigonometric cosine function<br>
 *    40. tg                  &lt;Unary Function&gt;        tg(x)                                         1.0   Trigonometric tangent function<br>
 *    41. tan                 &lt;Unary Function&gt;        tan(x)                                        1.0   Trigonometric tangent function<br>
 *    42. ctg                 &lt;Unary Function&gt;        ctg(x)                                        1.0   Trigonometric cotangent function<br>
 *    43. cot                 &lt;Unary Function&gt;        cot(x)                                        1.0   Trigonometric cotangent function<br>
 *    44. ctan                &lt;Unary Function&gt;        ctan(x)                                       1.0   Trigonometric cotangent function<br>
 *    45. sec                 &lt;Unary Function&gt;        sec(x)                                        1.0   Trigonometric secant function<br>
 *    46. csc                 &lt;Unary Function&gt;        csc(x)                                        1.0   Trigonometric cosecant function<br>
 *    47. cosec               &lt;Unary Function&gt;        cosec(x)                                      1.0   Trigonometric cosecant function<br>
 *    48. asin                &lt;Unary Function&gt;        asin(x)                                       1.0   Inverse trigonometric sine function<br>
 *    49. arsin               &lt;Unary Function&gt;        arsin(x)                                      1.0   Inverse trigonometric sine function<br>
 *    50. arcsin              &lt;Unary Function&gt;        arcsin(x)                                     1.0   Inverse trigonometric sine function<br>
 *    51. acos                &lt;Unary Function&gt;        acos(x)                                       1.0   Inverse trigonometric cosine function<br>
 *    52. arcos               &lt;Unary Function&gt;        arcos(x)                                      1.0   Inverse trigonometric cosine function<br>
 *    53. arccos              &lt;Unary Function&gt;        arccos(x)                                     1.0   Inverse trigonometric cosine function<br>
 *    54. atg                 &lt;Unary Function&gt;        atg(x)                                        1.0   Inverse trigonometric tangent function<br>
 *    55. atan                &lt;Unary Function&gt;        atan(x)                                       1.0   Inverse trigonometric tangent function<br>
 *    56. arctg               &lt;Unary Function&gt;        arctg(x)                                      1.0   Inverse trigonometric tangent function<br>
 *    57. arctan              &lt;Unary Function&gt;        arctan(x)                                     1.0   Inverse trigonometric tangent function<br>
 *    58. actg                &lt;Unary Function&gt;        actg(x)                                       1.0   Inverse trigonometric cotangent function<br>
 *    59. acot                &lt;Unary Function&gt;        acot(x)                                       1.0   Inverse trigonometric cotangent function<br>
 *    60. actan               &lt;Unary Function&gt;        actan(x)                                      1.0   Inverse trigonometric cotangent function<br>
 *    61. arcctg              &lt;Unary Function&gt;        arcctg(x)                                     1.0   Inverse trigonometric cotangent function<br>
 *    62. arccot              &lt;Unary Function&gt;        arccot(x)                                     1.0   Inverse trigonometric cotangent function<br>
 *    63. arcctan             &lt;Unary Function&gt;        arcctan(x)                                    1.0   Inverse trigonometric cotangent function<br>
 *    64. ln                  &lt;Unary Function&gt;        ln(x)                                         1.0   Natural logarithm function (base e)<br>
 *    65. log2                &lt;Unary Function&gt;        log2(x)                                       1.0   Binary logarithm function (base 2)<br>
 *    66. log10               &lt;Unary Function&gt;        log10(x)                                      1.0   Common logarithm function (base 10)<br>
 *    67. rad                 &lt;Unary Function&gt;        rad(x)                                        1.0   Degrees to radians function<br>
 *    68. exp                 &lt;Unary Function&gt;        exp(x)                                        1.0   Exponential function<br>
 *    69. sqrt                &lt;Unary Function&gt;        sqrt(x)                                       1.0   Squre root function<br>
 *    70. sinh                &lt;Unary Function&gt;        sinh(x)                                       1.0   Hyperbolic sine function<br>
 *    71. cosh                &lt;Unary Function&gt;        cosh(x)                                       1.0   Hyperbolic cosine function<br>
 *    72. tgh                 &lt;Unary Function&gt;        tgh(x)                                        1.0   Hyperbolic tangent function<br>
 *    73. tanh                &lt;Unary Function&gt;        tanh(x)                                       1.0   Hyperbolic tangent function<br>
 *    74. coth                &lt;Unary Function&gt;        coth(x)                                       1.0   Hyperbolic cotangent function<br>
 *    75. ctgh                &lt;Unary Function&gt;        ctgh(x)                                       1.0   Hyperbolic cotangent function<br>
 *    76. ctanh               &lt;Unary Function&gt;        ctanh(x)                                      1.0   Hyperbolic cotangent function<br>
 *    77. sech                &lt;Unary Function&gt;        sech(x)                                       1.0   Hyperbolic secant function<br>
 *    78. csch                &lt;Unary Function&gt;        csch(x)                                       1.0   Hyperbolic cosecant function<br>
 *    79. cosech              &lt;Unary Function&gt;        cosech(x)                                     1.0   Hyperbolic cosecant function<br>
 *    80. deg                 &lt;Unary Function&gt;        deg(x)                                        1.0   Radians to degrees function<br>
 *    81. abs                 &lt;Unary Function&gt;        abs(x)                                        1.0   Absolut value function<br>
 *    82. sgn                 &lt;Unary Function&gt;        sgn(x)                                        1.0   Signum function<br>
 *    83. floor               &lt;Unary Function&gt;        floor(x)                                      1.0   Floor function<br>
 *    84. ceil                &lt;Unary Function&gt;        ceil(x)                                       1.0   Ceiling function<br>
 *    85. not                 &lt;Unary Function&gt;        not(x)                                        1.0   Negation function<br>
 *    86. asinh               &lt;Unary Function&gt;        asinh(x)                                      1.0   Inverse hyperbolic sine function<br>
 *    87. arsinh              &lt;Unary Function&gt;        arsinh(x)                                     1.0   Inverse hyperbolic sine function<br>
 *    88. arcsinh             &lt;Unary Function&gt;        arcsinh(x)                                    1.0   Inverse hyperbolic sine function<br>
 *    89. acosh               &lt;Unary Function&gt;        acosh(x)                                      1.0   Inverse hyperbolic cosine function<br>
 *    90. arcosh              &lt;Unary Function&gt;        arcosh(x)                                     1.0   Inverse hyperbolic cosine function<br>
 *    91. arccosh             &lt;Unary Function&gt;        arccosh(x)                                    1.0   Inverse hyperbolic cosine function<br>
 *    92. atgh                &lt;Unary Function&gt;        atgh(x)                                       1.0   Inverse hyperbolic tangent function<br>
 *    93. atanh               &lt;Unary Function&gt;        atanh(x)                                      1.0   Inverse hyperbolic tangent function<br>
 *    94. arctgh              &lt;Unary Function&gt;        arctgh(x)                                     1.0   Inverse hyperbolic tangent function<br>
 *    95. arctanh             &lt;Unary Function&gt;        arctanh(x)                                    1.0   Inverse hyperbolic tangent function<br>
 *    96. acoth               &lt;Unary Function&gt;        acoth(x)                                      1.0   Inverse hyperbolic cotangent function<br>
 *    97. actgh               &lt;Unary Function&gt;        actgh(x)                                      1.0   Inverse hyperbolic cotangent function<br>
 *    98. actanh              &lt;Unary Function&gt;        actanh(x)                                     1.0   Inverse hyperbolic cotangent function<br>
 *    99. arcoth              &lt;Unary Function&gt;        arcoth(x)                                     1.0   Inverse hyperbolic cotangent function<br>
 *   100. arccoth             &lt;Unary Function&gt;        arccoth(x)                                    1.0   Inverse hyperbolic cotangent function<br>
 *   101. arcctgh             &lt;Unary Function&gt;        arcctgh(x)                                    1.0   Inverse hyperbolic cotangent function<br>
 *   102. arcctanh            &lt;Unary Function&gt;        arcctanh(x)                                   1.0   Inverse hyperbolic cotangent function<br>
 *   103. asech               &lt;Unary Function&gt;        asech(x)                                      1.0   Inverse hyperbolic secant function<br>
 *   104. arsech              &lt;Unary Function&gt;        arsech(x)                                     1.0   Inverse hyperbolic secant function<br>
 *   105. arcsech             &lt;Unary Function&gt;        arcsech(x)                                    1.0   Inverse hyperbolic secant function<br>
 *   106. acsch               &lt;Unary Function&gt;        acsch(x)                                      1.0   Inverse hyperbolic cosecant function<br>
 *   107. arcsch              &lt;Unary Function&gt;        arcsch(x)                                     1.0   Inverse hyperbolic cosecant function<br>
 *   108. arccsch             &lt;Unary Function&gt;        arccsch(x)                                    1.0   Inverse hyperbolic cosecant function<br>
 *   109. acosech             &lt;Unary Function&gt;        acosech(x)                                    1.0   Inverse hyperbolic cosecant function<br>
 *   110. arcosech            &lt;Unary Function&gt;        arcosech(x)                                   1.0   Inverse hyperbolic cosecant function<br>
 *   111. arccosech           &lt;Unary Function&gt;        arccosech(x)                                  1.0   Inverse hyperbolic cosecant function<br>
 *   112. Sa                  &lt;Unary Function&gt;        Sa(x)                                         1.0   Sinc function (normalized)<br>
 *   113. sinc                &lt;Unary Function&gt;        sinc(x)                                       1.0   Sinc function (normalized)<br>
 *   114. Sinc                &lt;Unary Function&gt;        Sinc(x)                                       1.0   Sinc function (unnormalized)<br>
 *   115. Bell                &lt;Unary Function&gt;        Bell(n)                                       1.0   Bell number<br>
 *   116. Luc                 &lt;Unary Function&gt;        Luc(n)                                        1.0   Lucas number<br>
 *   117. Fib                 &lt;Unary Function&gt;        Fib(n)                                        1.0   Fibonacci number<br>
 *   118. harm                &lt;Unary Function&gt;        harm(n)                                       1.0   Harmonic number<br>
 *   119. ispr                &lt;Unary Function&gt;        ispr(n)                                       2.3   Prime number test (is number a prime?)<br>
 *   120. Pi                  &lt;Unary Function&gt;        Pi(n)                                         2.3   Prime-counting function - Pi(x)<br>
 *   121. Ei                  &lt;Unary Function&gt;        Ei(x)                                         2.3   Exponential integral function (non-elementary special function) - usage example: Ei(x)<br>
 *   122. li                  &lt;Unary Function&gt;        li(x)                                         2.3   Logarithmic integral function (non-elementary special function) - usage example: li(x)<br>
 *   123. Li                  &lt;Unary Function&gt;        Li(x)                                         2.3   Offset logarithmic integral function (non-elementary special function) - usage example: Li(x)<br>
 *   124. erf                 &lt;Unary Function&gt;        erf(x)                                        3.0   Gauss error function (non-elementary special function) - usage example: 2 + erf(x)<br>
 *   125. erfc                &lt;Unary Function&gt;        erfc(x)                                       3.0   Gauss complementary error function (non-elementary special function) - usage example: 1 - erfc(x)<br>
 *   126. erfInv              &lt;Unary Function&gt;        erfInv(x)                                     3.0   Inverse Gauss error function (non-elementary special function) - usage example: erfInv(x)<br>
 *   127. erfcInv             &lt;Unary Function&gt;        erfcInv(x)                                    3.0   Inverse Gauss complementary error function (non-elementary special function) - usage example: erfcInv(x)<br>
 *   128. ulp                 &lt;Unary Function&gt;        ulp(x)                                        3.0   Unit in The Last Place - ulp(0.1)<br>
 *   129. isNaN               &lt;Unary Function&gt;        isNaN(x)                                      4.1   Returns true = 1 if value is a Not-a-Number (NaN), false = 0 otherwise - usage example: isNaN(x)<br>
 *   130. ndig10              &lt;Unary Function&gt;        ndig10(x)                                     4.1   Number of digits in numeral system with base 10<br>
 *   131. nfact               &lt;Unary Function&gt;        nfact(x)                                      4.1   Prime decomposition - number of distinct prime factors<br>
 *   132. arcsec              &lt;Unary Function&gt;        arcsec(x)                                     4.1   Inverse trigonometric secant<br>
 *   133. arccsc              &lt;Unary Function&gt;        arccsc(x)                                     4.1   Inverse trigonometric cosecant<br>
 *   134. log                 &lt;Binary Function&gt;       log(a, b)                                     1.0   Logarithm function<br>
 *   135. mod                 &lt;Binary Function&gt;       mod(a, b)                                     1.0   Modulo function<br>
 *   136. C                   &lt;Binary Function&gt;       C(n, k)                                       1.0   Binomial coefficient function<br>
 *   137. Bern                &lt;Binary Function&gt;       Bern(m, n)                                    1.0   Bernoulli numbers<br>
 *   138. Stirl1              &lt;Binary Function&gt;       Stirl1(n, k)                                  1.0   Stirling numbers of the first kind<br>
 *   139. Stirl2              &lt;Binary Function&gt;       Stirl2(n, k)                                  1.0   Stirling numbers of the second kind<br>
 *   140. Worp                &lt;Binary Function&gt;       Worp(n, k)                                    1.0   Worpitzky number<br>
 *   141. Euler               &lt;Binary Function&gt;       Euler(n, k)                                   1.0   Euler number<br>
 *   142. KDelta              &lt;Binary Function&gt;       KDelta(i, j)                                  1.0   Kronecker delta<br>
 *   143. EulerPol            &lt;Binary Function&gt;       EulerPol                                      1.0   EulerPol<br>
 *   144. Harm                &lt;Binary Function&gt;       Harm(x, n)                                    1.0   Harmonic number<br>
 *   145. rUni                &lt;Binary Function&gt;       rUni(a, b)                                    3.0   Random variable - Uniform continuous distribution U(a,b), usage example: 2*rUni(2,10)<br>
 *   146. rUnid               &lt;Binary Function&gt;       rUnid(a, b)                                   3.0   Random variable - Uniform discrete distribution U{a,b}, usage example: 2*rUnid(2,100)<br>
 *   147. round               &lt;Binary Function&gt;       round(x, n)                                   3.0   Half-up rounding, usage examples: round(2.2, 0) = 2, round(2.6, 0) = 3, round(2.66,1) = 2.7<br>
 *   148. rNor                &lt;Binary Function&gt;       rNor(mean, stdv)                              3.0   Random variable - Normal distribution N(m,s) m - mean, s - stddev, usage example: 3*rNor(0,1)<br>
 *   149. ndig                &lt;Binary Function&gt;       ndig(number, base)                            4.1   Number of digits representing the number in numeral system with given base<br>
 *   150. dig10               &lt;Binary Function&gt;       dig10(num, pos)                               4.1   Digit at position 1 ... n (left -&gt; right) or 0 ... -(n-1) (right -&gt; left) - base 10 numeral system<br>
 *   151. factval             &lt;Binary Function&gt;       factval(number, factorid)                     4.1   Prime decomposition - factor value at position between 1 ... nfact(n) - ascending order by factor value<br>
 *   152. factexp             &lt;Binary Function&gt;       factexp(number, factorid)                     4.1   Prime decomposition - factor exponent / multiplicity at position between 1 ... nfact(n) - ascending order by factor value<br>
 *   153. root                &lt;Binary Function&gt;       root(rootorder, number)                       4.1   N-th order root of a number<br>
 *   154. if                  &lt;3-args Function&gt;       if( cond, expr-if-true, expr-if-false )       1.0   If function<br>
 *   155. chi                 &lt;3-args Function&gt;       chi(x, a, b)                                  1.0   Characteristic function for x in (a,b)<br>
 *   156. CHi                 &lt;3-args Function&gt;       CHi(x, a, b)                                  1.0   Characteristic function for x in [a,b]<br>
 *   157. Chi                 &lt;3-args Function&gt;       Chi(x, a, b)                                  1.0   Characteristic function for x in [a,b)<br>
 *   158. cHi                 &lt;3-args Function&gt;       cHi(x, a, b)                                  1.0   Characteristic function for x in (a,b]<br>
 *   159. pUni                &lt;3-args Function&gt;       pUni(x, a, b)                                 3.0   Probability distribution function - Uniform continuous distribution U(a,b)<br>
 *   160. cUni                &lt;3-args Function&gt;       cUni(x, a, b)                                 3.0   Cumulative distribution function - Uniform continuous distribution U(a,b)<br>
 *   161. qUni                &lt;3-args Function&gt;       qUni(q, a, b)                                 3.0   Quantile function (inverse cumulative distribution function) - Uniform continuous distribution U(a,b)<br>
 *   162. pNor                &lt;3-args Function&gt;       pNor(x, mean, stdv)                           3.0   Probability distribution function - Normal distribution N(m,s)<br>
 *   163. cNor                &lt;3-args Function&gt;       cNor(x, mean, stdv)                           3.0   Cumulative distribution function - Normal distribution N(m,s)<br>
 *   164. qNor                &lt;3-args Function&gt;       qNor(q, mean, stdv)                           3.0   Quantile function (inverse cumulative distribution function)<br>
 *   165. dig                 &lt;3-args Function&gt;       dig(num, pos, base)                           4.1   Digit at position 1 ... n (left -&gt; right) or 0 ... -(n-1) (right -&gt; left) - numeral system with given base<br>
 *   166. iff                 &lt;Variadic Function&gt;     iff( cond-1, expr-1; ... ; cond-n, expr-n )   1.0   If function<br>
 *   167. min                 &lt;Variadic Function&gt;     min(a1, ..., an)                              1.0   Minimum function<br>
 *   168. max                 &lt;Variadic Function&gt;     max(a1, ..., an)                              1.0   Maximum function<br>
 *   169. ConFrac             &lt;Variadic Function&gt;     ConFrac(a1, ..., an)                          1.0   Continued fraction<br>
 *   170. ConPol              &lt;Variadic Function&gt;     ConPol(a1, ..., an)                           1.0   Continued polynomial<br>
 *   171. gcd                 &lt;Variadic Function&gt;     gcd(a1, ..., an)                              1.0   Greatest common divisor<br>
 *   172. lcm                 &lt;Variadic Function&gt;     lcm(a1, ..., an)                              1.0   Least common multiple<br>
 *   173. add                 &lt;Variadic Function&gt;     add(a1, ..., an)                              2.4   Summation operator<br>
 *   174. multi               &lt;Variadic Function&gt;     multi(a1, ..., an)                            2.4   Multiplication<br>
 *   175. mean                &lt;Variadic Function&gt;     mean(a1, ..., an)                             2.4   Mean / average value<br>
 *   176. var                 &lt;Variadic Function&gt;     var(a1, ..., an)                              2.4   Bias-corrected sample variance<br>
 *   177. std                 &lt;Variadic Function&gt;     std(a1, ..., an)                              2.4   Bias-corrected sample standard deviation<br>
 *   178. rList               &lt;Variadic Function&gt;     rList(a1, ..., an)                            3.0   Random number from given list of numbers<br>
 *   179. coalesce            &lt;Variadic Function&gt;     coalesce(a1, ..., an)                         4.1   Returns the first non-NaN value<br>
 *   180. or                  &lt;Variadic Function&gt;     or(a1, ..., an)                               4.1   Logical disjunction (OR) - variadic<br>
 *   181. and                 &lt;Variadic Function&gt;     and(a1, ..., an)                              4.1   Logical conjunction (AND) - variadic<br>
 *   182. xor                 &lt;Variadic Function&gt;     xor(a1, ..., an)                              4.1   Exclusive or (XOR) - variadic<br>
 *   183. argmin              &lt;Variadic Function&gt;     argmin(a1, ..., an)                           4.1   Arguments / indices of the minima<br>
 *   184. argmax              &lt;Variadic Function&gt;     argmax(a1, ..., an)                           4.1   Arguments / indices of the maxima<br>
 *   185. med                 &lt;Variadic Function&gt;     med(a1, ..., an)                              4.1   The sample median<br>
 *   186. mode                &lt;Variadic Function&gt;     mode(a1, ..., an)                             4.1   Mode - the value that appears most often<br>
 *   187. base                &lt;Variadic Function&gt;     base(b, d1, ..., dn)                          4.1   Returns number in given numeral system base represented by list of digits<br>
 *   188. ndist               &lt;Variadic Function&gt;     ndist(v1, ..., vn)                            4.1   Number of distinct values<br>
 *   189. sum                 &lt;Calculus Operator&gt;     sum( i, from, to, expr , &lt;by&gt; )               1.0   Summation operator - SIGMA<br>
 *   190. prod                &lt;Calculus Operator&gt;     prod( i, from, to, expr , &lt;by&gt; )              1.0   Product operator - PI<br>
 *   191. int                 &lt;Calculus Operator&gt;     int( expr, arg, from, to )                    1.0   Definite integral operator<br>
 *   192. der                 &lt;Calculus Operator&gt;     der( expr, arg, &lt;point&gt; )                     1.0   Derivative operator<br>
 *   193. der-                &lt;Calculus Operator&gt;     der-( expr, arg, &lt;point&gt; )                    1.0   Left derivative operator<br>
 *   194. der+                &lt;Calculus Operator&gt;     der+( expr, arg, &lt;point&gt; )                    1.0   Right derivative operator<br>
 *   195. dern                &lt;Calculus Operator&gt;     dern( expr, n, arg )                          1.0   n-th derivative operator<br>
 *   196. diff                &lt;Calculus Operator&gt;     diff( expr, arg, &lt;delta&gt; )                    1.0   Forward difference operator<br>
 *   197. difb                &lt;Calculus Operator&gt;     difb( expr, arg, &lt;delta&gt; )                    1.0   Backward difference operator<br>
 *   198. avg                 &lt;Calculus Operator&gt;     avg( i, from, to, expr , &lt;by&gt; )               2.4   Average operator<br>
 *   199. vari                &lt;Calculus Operator&gt;     vari( i, from, to, expr , &lt;by&gt; )              2.4   Bias-corrected sample variance operator<br>
 *   200. stdi                &lt;Calculus Operator&gt;     stdi( i, from, to, expr , &lt;by&gt; )              2.4   Bias-corrected sample standard deviation operator<br>
 *   201. mini                &lt;Calculus Operator&gt;     mini( i, from, to, expr , &lt;by&gt; )              2.4   Minimum value<br>
 *   202. maxi                &lt;Calculus Operator&gt;     maxi( i, from, to, expr , &lt;by&gt; )              2.4   Maximum value<br>
 *   203. solve               &lt;Calculus Operator&gt;     solve( expr, a, b )                           4.0   f(x) = 0 equation solving, function root finding<br>
 *   204. pi                  &lt;Constant Value&gt;        pi                                            1.0   Pi, Archimedes' constant or Ludolph's number<br>
 *   205. e                   &lt;Constant Value&gt;        e                                             1.0   Napier's constant, or Euler's number, base of Natural logarithm<br>
 *   206. [gam]               &lt;Constant Value&gt;        [gam]                                         1.0   Euler-Mascheroni constant<br>
 *   207. [phi]               &lt;Constant Value&gt;        [phi]                                         1.0   Golden ratio<br>
 *   208. [PN]                &lt;Constant Value&gt;        [PN]                                          1.0   Plastic constant<br>
 *   209. [B*]                &lt;Constant Value&gt;        [B*]                                          1.0   Embree-Trefethen constant<br>
 *   210. [F'd]               &lt;Constant Value&gt;        [F'd]                                         1.0   Feigenbaum constant alfa<br>
 *   211. [F'a]               &lt;Constant Value&gt;        [F'a]                                         1.0   Feigenbaum constant delta<br>
 *   212. [C2]                &lt;Constant Value&gt;        [C2]                                          1.0   Twin prime constant<br>
 *   213. [M1]                &lt;Constant Value&gt;        [M1]                                          1.0   Meissel-Mertens constant<br>
 *   214. [B2]                &lt;Constant Value&gt;        [B2]                                          1.0   Brun's constant for twin primes<br>
 *   215. [B4]                &lt;Constant Value&gt;        [B4]                                          1.0   Brun's constant for prime quadruplets<br>
 *   216. [BN'L]              &lt;Constant Value&gt;        [BN'L]                                        1.0   de Bruijn-Newman constant<br>
 *   217. [Kat]               &lt;Constant Value&gt;        [Kat]                                         1.0   Catalan's constant<br>
 *   218. [K*]                &lt;Constant Value&gt;        [K*]                                          1.0   Landau-Ramanujan constant<br>
 *   219. [K.]                &lt;Constant Value&gt;        [K.]                                          1.0   Viswanath's constant<br>
 *   220. [B'L]               &lt;Constant Value&gt;        [B'L]                                         1.0   Legendre's constant<br>
 *   221. [RS'm]              &lt;Constant Value&gt;        [RS'm]                                        1.0   Ramanujan-Soldner constant<br>
 *   222. [EB'e]              &lt;Constant Value&gt;        [EB'e]                                        1.0   Erdos-Borwein constant<br>
 *   223. [Bern]              &lt;Constant Value&gt;        [Bern]                                        1.0   Bernstein's constant<br>
 *   224. [GKW'l]             &lt;Constant Value&gt;        [GKW'l]                                       1.0   Gauss-Kuzmin-Wirsing constant<br>
 *   225. [HSM's]             &lt;Constant Value&gt;        [HSM's]                                       1.0   Hafner-Sarnak-McCurley constant<br>
 *   226. [lm]                &lt;Constant Value&gt;        [lm]                                          1.0   Golomb-Dickman constant<br>
 *   227. [Cah]               &lt;Constant Value&gt;        [Cah]                                         1.0   Cahen's constant<br>
 *   228. [Ll]                &lt;Constant Value&gt;        [Ll]                                          1.0   Laplace limit<br>
 *   229. [AG]                &lt;Constant Value&gt;        [AG]                                          1.0   Alladi-Grinstead constant<br>
 *   230. [L*]                &lt;Constant Value&gt;        [L*]                                          1.0   Lengyel's constant<br>
 *   231. [L.]                &lt;Constant Value&gt;        [L.]                                          1.0   Levy's constant<br>
 *   232. [Dz3]               &lt;Constant Value&gt;        [Dz3]                                         1.0   Apery's constant<br>
 *   233. [A3n]               &lt;Constant Value&gt;        [A3n]                                         1.0   Mills' constant<br>
 *   234. [Bh]                &lt;Constant Value&gt;        [Bh]                                          1.0   Backhouse's constant<br>
 *   235. [Pt]                &lt;Constant Value&gt;        [Pt]                                          1.0   Porter's constant<br>
 *   236. [L2]                &lt;Constant Value&gt;        [L2]                                          1.0   Lieb's square ice constant<br>
 *   237. [Nv]                &lt;Constant Value&gt;        [Nv]                                          1.0   Niven's constant<br>
 *   238. [Ks]                &lt;Constant Value&gt;        [Ks]                                          1.0   Sierpinski's constant<br>
 *   239. [Kh]                &lt;Constant Value&gt;        [Kh]                                          1.0   Khinchin's constant<br>
 *   240. [FR]                &lt;Constant Value&gt;        [FR]                                          1.0   Fransen-Robinson constant<br>
 *   241. [La]                &lt;Constant Value&gt;        [La]                                          1.0   Landau's constant<br>
 *   242. [P2]                &lt;Constant Value&gt;        [P2]                                          1.0   Parabolic constant<br>
 *   243. [Om]                &lt;Constant Value&gt;        [Om]                                          1.0   Omega constant<br>
 *   244. [MRB]               &lt;Constant Value&gt;        [MRB]                                         1.0   MRB constant<br>
 *   245. [li2]               &lt;Constant Value&gt;        [li2]                                         2.3   li(2) - Logarithmic integral function at x=2<br>
 *   246. [EG]                &lt;Constant Value&gt;        [EG]                                          2.3   Gompertz constant<br>
 *   247. [c]                 &lt;Constant Value&gt;        [c]                                           4.0   &lt;Physical Constant&gt; Light speed in vacuum [m/s] (m=1, s=1)<br>
 *   248. [G.]                &lt;Constant Value&gt;        [G.]                                          4.0   &lt;Physical Constant&gt; Gravitational constant (m=1, kg=1, s=1)]<br>
 *   249. [g]                 &lt;Constant Value&gt;        [g]                                           4.0   &lt;Physical Constant&gt; Gravitational acceleration on Earth [m/s^2] (m=1, s=1)<br>
 *   250. [hP]                &lt;Constant Value&gt;        [hP]                                          4.0   &lt;Physical Constant&gt; Planck constant (m=1, kg=1, s=1)<br>
 *   251. [h-]                &lt;Constant Value&gt;        [h-]                                          4.0   &lt;Physical Constant&gt; Reduced Planck constant / Dirac constant (m=1, kg=1, s=1)]<br>
 *   252. [lP]                &lt;Constant Value&gt;        [lP]                                          4.0   &lt;Physical Constant&gt; Planck length [m] (m=1)<br>
 *   253. [mP]                &lt;Constant Value&gt;        [mP]                                          4.0   &lt;Physical Constant&gt; Planck mass [kg] (kg=1)<br>
 *   254. [tP]                &lt;Constant Value&gt;        [tP]                                          4.0   &lt;Physical Constant&gt; Planck time [s] (s=1)<br>
 *   255. [ly]                &lt;Constant Value&gt;        [ly]                                          4.0   &lt;Astronomical Constant&gt; Light year [m] (m=1)<br>
 *   256. [au]                &lt;Constant Value&gt;        [au]                                          4.0   &lt;Astronomical Constant&gt; Astronomical unit [m] (m=1)<br>
 *   257. [pc]                &lt;Constant Value&gt;        [pc]                                          4.0   &lt;Astronomical Constant&gt; Parsec [m] (m=1)<br>
 *   258. [kpc]               &lt;Constant Value&gt;        [kpc]                                         4.0   &lt;Astronomical Constant&gt; Kiloparsec [m] (m=1)<br>
 *   259. [Earth-R-eq]        &lt;Constant Value&gt;        [Earth-R-eq]                                  4.0   &lt;Astronomical Constant&gt; Earth equatorial radius [m] (m=1)<br>
 *   260. [Earth-R-po]        &lt;Constant Value&gt;        [Earth-R-po]                                  4.0   &lt;Astronomical Constant&gt; Earth polar radius [m] (m=1)<br>
 *   261. [Earth-R]           &lt;Constant Value&gt;        [Earth-R]                                     4.0   &lt;Astronomical Constant&gt; Earth mean radius (m=1)<br>
 *   262. [Earth-M]           &lt;Constant Value&gt;        [Earth-M]                                     4.0   &lt;Astronomical Constant&gt; Earth mass [kg] (kg=1)<br>
 *   263. [Earth-D]           &lt;Constant Value&gt;        [Earth-D]                                     4.0   &lt;Astronomical Constant&gt; Earth-Sun distance - semi major axis [m] (m=1)<br>
 *   264. [Moon-R]            &lt;Constant Value&gt;        [Moon-R]                                      4.0   &lt;Astronomical Constant&gt; Moon mean radius [m] (m=1)<br>
 *   265. [Moon-M]            &lt;Constant Value&gt;        [Moon-M]                                      4.0   &lt;Astronomical Constant&gt; Moon mass [kg] (kg=1)<br>
 *   266. [Moon-D]            &lt;Constant Value&gt;        [Moon-D]                                      4.0   &lt;Astronomical Constant&gt; Moon-Earth distance - semi major axis [m] (m=1)<br>
 *   267. [Solar-R]           &lt;Constant Value&gt;        [Solar-R]                                     4.0   &lt;Astronomical Constant&gt; Solar mean radius [m] (m=1)<br>
 *   268. [Solar-M]           &lt;Constant Value&gt;        [Solar-M]                                     4.0   &lt;Astronomical Constant&gt; Solar mass [kg] (kg=1)<br>
 *   269. [Mercury-R]         &lt;Constant Value&gt;        [Mercury-R]                                   4.0   &lt;Astronomical Constant&gt; Mercury mean radius [m] (m=1)<br>
 *   270. [Mercury-M]         &lt;Constant Value&gt;        [Mercury-M]                                   4.0   &lt;Astronomical Constant&gt; Mercury mass [kg] (kg=1)<br>
 *   271. [Mercury-D]         &lt;Constant Value&gt;        [Mercury-D]                                   4.0   &lt;Astronomical Constant&gt; Mercury-Sun distance - semi major axis [m] (m=1)<br>
 *   272. [Venus-R]           &lt;Constant Value&gt;        [Venus-R]                                     4.0   &lt;Astronomical Constant&gt; Venus mean radius [m] (m=1)<br>
 *   273. [Venus-M]           &lt;Constant Value&gt;        [Venus-M]                                     4.0   &lt;Astronomical Constant&gt; Venus mass [kg] (kg=1)<br>
 *   274. [Venus-D]           &lt;Constant Value&gt;        [Venus-D]                                     4.0   &lt;Astronomical Constant&gt; Venus-Sun distance - semi major axis [m] (m=1)<br>
 *   275. [Mars-R]            &lt;Constant Value&gt;        [Mars-R]                                      4.0   &lt;Astronomical Constant&gt; Mars mean radius [m] (m=1)<br>
 *   276. [Mars-M]            &lt;Constant Value&gt;        [Mars-M]                                      4.0   &lt;Astronomical Constant&gt; Mars mass [kg] (kg=1)<br>
 *   277. [Mars-D]            &lt;Constant Value&gt;        [Mars-D]                                      4.0   &lt;Astronomical Constant&gt; Mars-Sun distance - semi major axis [m] (m=1)<br>
 *   278. [Jupiter-R]         &lt;Constant Value&gt;        [Jupiter-R]                                   4.0   &lt;Astronomical Constant&gt; Jupiter mean radius [m] (m=1)<br>
 *   279. [Jupiter-M]         &lt;Constant Value&gt;        [Jupiter-M]                                   4.0   &lt;Astronomical Constant&gt; Jupiter mass [kg] (kg=1)<br>
 *   280. [Jupiter-D]         &lt;Constant Value&gt;        [Jupiter-D]                                   4.0   &lt;Astronomical Constant&gt; Jupiter-Sun distance - semi major axis [m] (m=1)<br>
 *   281. [Saturn-R]          &lt;Constant Value&gt;        [Saturn-R]                                    4.0   &lt;Astronomical Constant&gt; Saturn mean radius [m] (m=1)<br>
 *   282. [Saturn-M]          &lt;Constant Value&gt;        [Saturn-M]                                    4.0   &lt;Astronomical Constant&gt; Saturn mass [kg] (kg=1)<br>
 *   283. [Saturn-D]          &lt;Constant Value&gt;        [Saturn-D]                                    4.0   &lt;Astronomical Constant&gt; Saturn-Sun distance - semi major axis [m] (m=1)<br>
 *   284. [Uranus-R]          &lt;Constant Value&gt;        [Uranus-R]                                    4.0   &lt;Astronomical Constant&gt; Uranus mean radius [m] (m=1)<br>
 *   285. [Uranus-M]          &lt;Constant Value&gt;        [Uranus-M]                                    4.0   &lt;Astronomical Constant&gt; Uranus mass [kg] (kg=1)<br>
 *   286. [Uranus-D]          &lt;Constant Value&gt;        [Uranus-D]                                    4.0   &lt;Astronomical Constant&gt; Uranus-Sun distance - semi major axis [m] (m=1)<br>
 *   287. [Neptune-R]         &lt;Constant Value&gt;        [Neptune-R]                                   4.0   &lt;Astronomical Constant&gt; Neptune mean radius [m] (m=1)<br>
 *   288. [Neptune-M]         &lt;Constant Value&gt;        [Neptune-M]                                   4.0   &lt;Astronomical Constant&gt; Neptune mass [kg] (kg=1)<br>
 *   289. [Neptune-D]         &lt;Constant Value&gt;        [Neptune-D]                                   4.0   &lt;Astronomical Constant&gt; Neptune-Sun distance - semi major axis [m] (m=1)<br>
 *   290. [true]              &lt;Constant Value&gt;        [true]                                        4.1   Boolean True represented as double, [true] = 1<br>
 *   291. [false]             &lt;Constant Value&gt;        [false]                                       4.1   Boolean False represented as double, [false] = 0<br>
 *   292. [NaN]               &lt;Constant Value&gt;        [NaN]                                         4.1   Not-a-Number<br>
 *   293. [Uni]               &lt;Random Variable&gt;       [Uni]                                         3.0   Random variable - Uniform continuous distribution U(0,1)<br>
 *   294. [Int]               &lt;Random Variable&gt;       [Int]                                         3.0   Random variable - random integer<br>
 *   295. [Int1]              &lt;Random Variable&gt;       [Int1]                                        3.0   Random variable - random integer - Uniform discrete distribution U{-10^1, 10^1}<br>
 *   296. [Int2]              &lt;Random Variable&gt;       [Int2]                                        3.0   Random variable - random integer - Uniform discrete distribution U{-10^2, 10^2}<br>
 *   297. [Int3]              &lt;Random Variable&gt;       [Int3]                                        3.0   Random variable - random integer - Uniform discrete distribution U{-10^3, 10^3}<br>
 *   298. [Int4]              &lt;Random Variable&gt;       [Int4]                                        3.0   Random variable - random integer - Uniform discrete distribution U{-10^4, 10^4}<br>
 *   299. [Int5]              &lt;Random Variable&gt;       [Int5]                                        3.0   Random variable - random integer - Uniform discrete distribution U{-10^5, 10^5}<br>
 *   300. [Int6]              &lt;Random Variable&gt;       [Int6]                                        3.0   Random variable - random integer - Uniform discrete distribution U{-10^6, 10^6}<br>
 *   301. [Int7]              &lt;Random Variable&gt;       [Int7]                                        3.0   Random variable - random integer - Uniform discrete distribution U{-10^7, 10^7}<br>
 *   302. [Int8]              &lt;Random Variable&gt;       [Int8]                                        3.0   Random variable - random integer - Uniform discrete distribution U{-10^8, 10^8}<br>
 *   303. [Int9]              &lt;Random Variable&gt;       [Int9]                                        3.0   Random variable - random integer - Uniform discrete distribution U{-10^9, 10^9}<br>
 *   304. [nat]               &lt;Random Variable&gt;       [nat]                                         3.0   Random variable - random natural number including 0<br>
 *   305. [nat1]              &lt;Random Variable&gt;       [nat1]                                        3.0   Random variable - random natural number including 0 - Uniform discrete distribution U{0, 10^1}<br>
 *   306. [nat2]              &lt;Random Variable&gt;       [nat2]                                        3.0   Random variable - random natural number including 0 - Uniform discrete distribution U{0, 10^2}<br>
 *   307. [nat3]              &lt;Random Variable&gt;       [nat3]                                        3.0   Random variable - random natural number including 0 - Uniform discrete distribution U{0, 10^3}<br>
 *   308. [nat4]              &lt;Random Variable&gt;       [nat4]                                        3.0   Random variable - random natural number including 0 - Uniform discrete distribution U{0, 10^4}<br>
 *   309. [nat5]              &lt;Random Variable&gt;       [nat5]                                        3.0   Random variable - random natural number including 0 - Uniform discrete distribution U{0, 10^5}<br>
 *   310. [nat6]              &lt;Random Variable&gt;       [nat6]                                        3.0   Random variable - random natural number including 0 - Uniform discrete distribution U{0, 10^6}<br>
 *   311. [nat7]              &lt;Random Variable&gt;       [nat7]                                        3.0   Random variable - random natural number including 0 - Uniform discrete distribution U{0, 10^7}<br>
 *   312. [nat8]              &lt;Random Variable&gt;       [nat8]                                        3.0   Random variable - random natural number including 0 - Uniform discrete distribution U{0, 10^8}<br>
 *   313. [nat9]              &lt;Random Variable&gt;       [nat9]                                        3.0   Random variable - random natural number including 0 - Uniform discrete distribution U{0, 10^9}<br>
 *   314. [Nat]               &lt;Random Variable&gt;       [Nat]                                         3.0   Random variable - random natural number<br>
 *   315. [Nat1]              &lt;Random Variable&gt;       [Nat1]                                        3.0   Random variable - random natural number - Uniform discrete distribution U{1, 10^1}<br>
 *   316. [Nat2]              &lt;Random Variable&gt;       [Nat2]                                        3.0   Random variable - random natural number - Uniform discrete distribution U{1, 10^2}<br>
 *   317. [Nat3]              &lt;Random Variable&gt;       [Nat3]                                        3.0   Random variable - random natural number - Uniform discrete distribution U{1, 10^3}<br>
 *   318. [Nat4]              &lt;Random Variable&gt;       [Nat4]                                        3.0   Random variable - random natural number - Uniform discrete distribution U{1, 10^4}<br>
 *   319. [Nat5]              &lt;Random Variable&gt;       [Nat5]                                        3.0   Random variable - random natural number - Uniform discrete distribution U{1, 10^5}<br>
 *   320. [Nat6]              &lt;Random Variable&gt;       [Nat6]                                        3.0   Random variable - random natural number - Uniform discrete distribution U{1, 10^6}<br>
 *   321. [Nat7]              &lt;Random Variable&gt;       [Nat7]                                        3.0   Random variable - random natural number - Uniform discrete distribution U{1, 10^7}<br>
 *   322. [Nat8]              &lt;Random Variable&gt;       [Nat8]                                        3.0   Random variable - random natural number - Uniform discrete distribution U{1, 10^8}<br>
 *   323. [Nat9]              &lt;Random Variable&gt;       [Nat9]                                        3.0   Random variable - random natural number - Uniform discrete distribution U{1, 10^9}<br>
 *   324. [Nor]               &lt;Random Variable&gt;       [Nor]                                         3.0   Random variable - Normal distribution N(0,1)<br>
 *   325. &#64;~                  &lt;Bitwise Operator&gt;      a &#64;~ b                                        4.0   Bitwise unary complement<br>
 *   326. &#64;&amp;                  &lt;Bitwise Operator&gt;      a &#64;&amp; b                                        4.0   Bitwise AND<br>
 *   327. &#64;^                  &lt;Bitwise Operator&gt;      a &#64;^ b                                        4.0   Bitwise exclusive OR<br>
 *   328. &#64;|                  &lt;Bitwise Operator&gt;      a &#64;| b                                        4.0   Bitwise inclusive OR<br>
 *   329. &#64;&lt;&lt;                 &lt;Bitwise Operator&gt;      a &#64;&lt;&lt; b                                       4.0   Signed left shift<br>
 *   330. &#64;&gt;&gt;                 &lt;Bitwise Operator&gt;      a &#64;&gt;&gt; b                                       4.0   Signed right shift<br>
 *   331. [%]                 &lt;Unit&gt;                  [%]                                           4.0   &lt;Ratio, Fraction&gt; Percentage = 0.01<br>
 *   332. [%%]                &lt;Unit&gt;                  [%%]                                          4.0   &lt;Ratio, Fraction&gt; Promil, Per mille = 0.001<br>
 *   333. [Y]                 &lt;Unit&gt;                  [Y]                                           4.0   &lt;Metric prefix&gt; Septillion / Yotta = 10^24<br>
 *   334. [sept]              &lt;Unit&gt;                  [sept]                                        4.0   &lt;Metric prefix&gt; Septillion / Yotta = 10^24<br>
 *   335. [Z]                 &lt;Unit&gt;                  [Z]                                           4.0   &lt;Metric prefix&gt; Sextillion / Zetta = 10^21<br>
 *   336. [sext]              &lt;Unit&gt;                  [sext]                                        4.0   &lt;Metric prefix&gt; Sextillion / Zetta = 10^21<br>
 *   337. [E]                 &lt;Unit&gt;                  [E]                                           4.0   &lt;Metric prefix&gt; Quintillion / Exa = 10^18<br>
 *   338. [quint]             &lt;Unit&gt;                  [quint]                                       4.0   &lt;Metric prefix&gt; Quintillion / Exa = 10^18<br>
 *   339. [P]                 &lt;Unit&gt;                  [P]                                           4.0   &lt;Metric prefix&gt; Quadrillion / Peta = 10^15<br>
 *   340. [quad]              &lt;Unit&gt;                  [quad]                                        4.0   &lt;Metric prefix&gt; Quadrillion / Peta = 10^15<br>
 *   341. [T]                 &lt;Unit&gt;                  [T]                                           4.0   &lt;Metric prefix&gt; Trillion / Tera = 10^12<br>
 *   342. [tril]              &lt;Unit&gt;                  [tril]                                        4.0   &lt;Metric prefix&gt; Trillion / Tera = 10^12<br>
 *   343. [G]                 &lt;Unit&gt;                  [G]                                           4.0   &lt;Metric prefix&gt; Billion / Giga = 10^9<br>
 *   344. [bil]               &lt;Unit&gt;                  [bil]                                         4.0   &lt;Metric prefix&gt; Billion / Giga = 10^9<br>
 *   345. [M]                 &lt;Unit&gt;                  [M]                                           4.0   &lt;Metric prefix&gt; Million / Mega = 10^6<br>
 *   346. [mil]               &lt;Unit&gt;                  [mil]                                         4.0   &lt;Metric prefix&gt; Million / Mega = 10^6<br>
 *   347. [k]                 &lt;Unit&gt;                  [k]                                           4.0   &lt;Metric prefix&gt; Thousand / Kilo = 10^3<br>
 *   348. [th]                &lt;Unit&gt;                  [th]                                          4.0   &lt;Metric prefix&gt; Thousand / Kilo = 10^3<br>
 *   349. [hund]              &lt;Unit&gt;                  [hund]                                        4.0   &lt;Metric prefix&gt; Hundred / Hecto = 10^2<br>
 *   350. [hecto]             &lt;Unit&gt;                  [hecto]                                       4.0   &lt;Metric prefix&gt; Hundred / Hecto = 10^2<br>
 *   351. [ten]               &lt;Unit&gt;                  [ten]                                         4.0   &lt;Metric prefix&gt; Ten / Deca = 10<br>
 *   352. [deca]              &lt;Unit&gt;                  [deca]                                        4.0   &lt;Metric prefix&gt; Ten / Deca = 10<br>
 *   353. [deci]              &lt;Unit&gt;                  [deci]                                        4.0   &lt;Metric prefix&gt; Tenth / Deci = 0.1<br>
 *   354. [centi]             &lt;Unit&gt;                  [centi]                                       4.0   &lt;Metric prefix&gt; Hundredth / Centi = 0.01<br>
 *   355. [milli]             &lt;Unit&gt;                  [milli]                                       4.0   &lt;Metric prefix&gt; Thousandth / Milli = 0.001<br>
 *   356. [mic]               &lt;Unit&gt;                  [mic]                                         4.0   &lt;Metric prefix&gt; Millionth / Micro = 10^-6<br>
 *   357. [n]                 &lt;Unit&gt;                  [n]                                           4.0   &lt;Metric prefix&gt; Billionth / Nano = 10^-9<br>
 *   358. [p]                 &lt;Unit&gt;                  [p]                                           4.0   &lt;Metric prefix&gt; Trillionth / Pico = 10^-12<br>
 *   359. [f]                 &lt;Unit&gt;                  [f]                                           4.0   &lt;Metric prefix&gt; Quadrillionth / Femto = 10^-15<br>
 *   360. [a]                 &lt;Unit&gt;                  [a]                                           4.0   &lt;Metric prefix&gt; Quintillionth / Atoo = 10^-18<br>
 *   361. [z]                 &lt;Unit&gt;                  [z]                                           4.0   &lt;Metric prefix&gt; Sextillionth / Zepto = 10^-21<br>
 *   362. [y]                 &lt;Unit&gt;                  [y]                                           4.0   &lt;Metric prefix&gt; Septillionth / Yocto = 10^-24<br>
 *   363. [m]                 &lt;Unit&gt;                  [m]                                           4.0   &lt;Unit of length&gt; Metre / Meter (m=1)<br>
 *   364. [km]                &lt;Unit&gt;                  [km]                                          4.0   &lt;Unit of length&gt; Kilometre / Kilometer (m=1)<br>
 *   365. [cm]                &lt;Unit&gt;                  [cm]                                          4.0   &lt;Unit of length&gt; Centimetre / Centimeter (m=1)<br>
 *   366. [mm]                &lt;Unit&gt;                  [mm]                                          4.0   &lt;Unit of length&gt; Millimetre / Millimeter (m=1)<br>
 *   367. [inch]              &lt;Unit&gt;                  [inch]                                        4.0   &lt;Unit of length&gt; Inch (m=1)<br>
 *   368. [yd]                &lt;Unit&gt;                  [yd]                                          4.0   &lt;Unit of length&gt; Yard (m=1)<br>
 *   369. [ft]                &lt;Unit&gt;                  [ft]                                          4.0   &lt;Unit of length&gt; Feet (m=1)<br>
 *   370. [mile]              &lt;Unit&gt;                  [mile]                                        4.0   &lt;Unit of length&gt; Mile (m=1)<br>
 *   371. [nmi]               &lt;Unit&gt;                  [nmi]                                         4.0   &lt;Unit of length&gt; Nautical mile (m=1)<br>
 *   372. [m2]                &lt;Unit&gt;                  [m2]                                          4.0   &lt;Unit of area&gt; Square metre / Square meter (m=1)<br>
 *   373. [cm2]               &lt;Unit&gt;                  [cm2]                                         4.0   &lt;Unit of area&gt; Square centimetre / Square centimeter (m=1)<br>
 *   374. [mm2]               &lt;Unit&gt;                  [mm2]                                         4.0   &lt;Unit of area&gt; Square millimetre / Square millimeter (m=1)<br>
 *   375. [are]               &lt;Unit&gt;                  [are]                                         4.0   &lt;Unit of area&gt; Are (m=1)<br>
 *   376. [ha]                &lt;Unit&gt;                  [ha]                                          4.0   &lt;Unit of area&gt; Hectare (m=1)<br>
 *   377. [acre]              &lt;Unit&gt;                  [acre]                                        4.0   &lt;Unit of area&gt; Acre (m=1)<br>
 *   378. [km2]               &lt;Unit&gt;                  [km2]                                         4.0   &lt;Unit of area&gt; Square kilometre / Square kilometer (m=1)<br>
 *   379. [mm3]               &lt;Unit&gt;                  [mm3]                                         4.0   &lt;Unit of volume&gt; Cubic millimetre / Cubic millimeter (m=1)<br>
 *   380. [cm3]               &lt;Unit&gt;                  [cm3]                                         4.0   &lt;Unit of volume&gt; Cubic centimetre / Cubic centimeter (m=1)<br>
 *   381. [m3]                &lt;Unit&gt;                  [m3]                                          4.0   &lt;Unit of volume&gt; Cubic metre / Cubic meter (m=1)<br>
 *   382. [km3]               &lt;Unit&gt;                  [km3]                                         4.0   &lt;Unit of volume&gt; Cubic kilometre / Cubic kilometer (m=1)<br>
 *   383. [ml]                &lt;Unit&gt;                  [ml]                                          4.0   &lt;Unit of volume&gt; Millilitre / Milliliter (m=1)<br>
 *   384. [l]                 &lt;Unit&gt;                  [l]                                           4.0   &lt;Unit of volume&gt; Litre / Liter (m=1)<br>
 *   385. [gall]              &lt;Unit&gt;                  [gall]                                        4.0   &lt;Unit of volume&gt; Gallon (m=1)<br>
 *   386. [pint]              &lt;Unit&gt;                  [pint]                                        4.0   &lt;Unit of volume&gt; Pint (m=1)<br>
 *   387. [s]                 &lt;Unit&gt;                  [s]                                           4.0   &lt;Unit of time&gt; Second (s=1)<br>
 *   388. [ms]                &lt;Unit&gt;                  [ms]                                          4.0   &lt;Unit of time&gt; Millisecond (s=1)<br>
 *   389. [min]               &lt;Unit&gt;                  [min]                                         4.0   &lt;Unit of time&gt; Minute (s=1)<br>
 *   390. [h]                 &lt;Unit&gt;                  [h]                                           4.0   &lt;Unit of time&gt; Hour (s=1)<br>
 *   391. [day]               &lt;Unit&gt;                  [day]                                         4.0   &lt;Unit of time&gt; Day (s=1)<br>
 *   392. [week]              &lt;Unit&gt;                  [week]                                        4.0   &lt;Unit of time&gt; Week (s=1)<br>
 *   393. [yearj]             &lt;Unit&gt;                  [yearj]                                       4.0   &lt;Unit of time&gt; Julian year = 365.25 days (s=1)<br>
 *   394. [kg]                &lt;Unit&gt;                  [kg]                                          4.0   &lt;Unit of mass&gt; Kilogram (kg=1)<br>
 *   395. [gr]                &lt;Unit&gt;                  [gr]                                          4.0   &lt;Unit of mass&gt; Gram (kg=1)<br>
 *   396. [mg]                &lt;Unit&gt;                  [mg]                                          4.0   &lt;Unit of mass&gt; Milligram (kg=1)<br>
 *   397. [dag]               &lt;Unit&gt;                  [dag]                                         4.0   &lt;Unit of mass&gt; Decagram (kg=1)<br>
 *   398. [t]                 &lt;Unit&gt;                  [t]                                           4.0   &lt;Unit of mass&gt; Tonne (kg=1)<br>
 *   399. [oz]                &lt;Unit&gt;                  [oz]                                          4.0   &lt;Unit of mass&gt; Ounce (kg=1)<br>
 *   400. [lb]                &lt;Unit&gt;                  [lb]                                          4.0   &lt;Unit of mass&gt; Pound (kg=1)<br>
 *   401. [b]                 &lt;Unit&gt;                  [b]                                           4.0   &lt;Unit of information&gt; Bit (bit=1)<br>
 *   402. [kb]                &lt;Unit&gt;                  [kb]                                          4.0   &lt;Unit of information&gt; Kilobit (bit=1)<br>
 *   403. [Mb]                &lt;Unit&gt;                  [Mb]                                          4.0   &lt;Unit of information&gt; Megabit (bit=1)<br>
 *   404. [Gb]                &lt;Unit&gt;                  [Gb]                                          4.0   &lt;Unit of information&gt; Gigabit (bit=1)<br>
 *   405. [Tb]                &lt;Unit&gt;                  [Tb]                                          4.0   &lt;Unit of information&gt; Terabit (bit=1)<br>
 *   406. [Pb]                &lt;Unit&gt;                  [Pb]                                          4.0   &lt;Unit of information&gt; Petabit (bit=1)<br>
 *   407. [Eb]                &lt;Unit&gt;                  [Eb]                                          4.0   &lt;Unit of information&gt; Exabit (bit=1)<br>
 *   408. [Zb]                &lt;Unit&gt;                  [Zb]                                          4.0   &lt;Unit of information&gt; Zettabit (bit=1)<br>
 *   409. [Yb]                &lt;Unit&gt;                  [Yb]                                          4.0   &lt;Unit of information&gt; Yottabit (bit=1)<br>
 *   410. [B]                 &lt;Unit&gt;                  [B]                                           4.0   &lt;Unit of information&gt; Byte (bit=1)<br>
 *   411. [kB]                &lt;Unit&gt;                  [kB]                                          4.0   &lt;Unit of information&gt; Kilobyte (bit=1)<br>
 *   412. [MB]                &lt;Unit&gt;                  [MB]                                          4.0   &lt;Unit of information&gt; Megabyte (bit=1)<br>
 *   413. [GB]                &lt;Unit&gt;                  [GB]                                          4.0   &lt;Unit of information&gt; Gigabyte (bit=1)<br>
 *   414. [TB]                &lt;Unit&gt;                  [TB]                                          4.0   &lt;Unit of information&gt; Terabyte (bit=1)<br>
 *   415. [PB]                &lt;Unit&gt;                  [PB]                                          4.0   &lt;Unit of information&gt; Petabyte (bit=1)<br>
 *   416. [EB]                &lt;Unit&gt;                  [EB]                                          4.0   &lt;Unit of information&gt; Exabyte (bit=1)<br>
 *   417. [ZB]                &lt;Unit&gt;                  [ZB]                                          4.0   &lt;Unit of information&gt; Zettabyte (bit=1)<br>
 *   418. [YB]                &lt;Unit&gt;                  [YB]                                          4.0   &lt;Unit of information&gt; Yottabyte (bit=1)<br>
 *   419. [J]                 &lt;Unit&gt;                  [J]                                           4.0   &lt;Unit of energy&gt; Joule (m=1, kg=1, s=1)<br>
 *   420. [eV]                &lt;Unit&gt;                  [eV]                                          4.0   &lt;Unit of energy&gt; Electronovolt (m=1, kg=1, s=1)<br>
 *   421. [keV]               &lt;Unit&gt;                  [keV]                                         4.0   &lt;Unit of energy&gt; Kiloelectronovolt (m=1, kg=1, s=1)<br>
 *   422. [MeV]               &lt;Unit&gt;                  [MeV]                                         4.0   &lt;Unit of energy&gt; Megaelectronovolt (m=1, kg=1, s=1)<br>
 *   423. [GeV]               &lt;Unit&gt;                  [GeV]                                         4.0   &lt;Unit of energy&gt; Gigaelectronovolt (m=1, kg=1, s=1)<br>
 *   424. [TeV]               &lt;Unit&gt;                  [TeV]                                         4.0   &lt;Unit of energy&gt; Teraelectronovolt (m=1, kg=1, s=1)<br>
 *   425. [m/s]               &lt;Unit&gt;                  [m/s]                                         4.0   &lt;Unit of speed&gt; Metre / Meter per second (m=1, s=1)<br>
 *   426. [km/h]              &lt;Unit&gt;                  [km/h]                                        4.0   &lt;Unit of speed&gt; Kilometre / Kilometer per hour (m=1, s=1)<br>
 *   427. [mi/h]              &lt;Unit&gt;                  [mi/h]                                        4.0   &lt;Unit of speed&gt; Mile per hour (m=1, s=1)<br>
 *   428. [knot]              &lt;Unit&gt;                  [knot]                                        4.0   &lt;Unit of speed&gt; Knot (m=1, s=1)<br>
 *   429. [m/s2]              &lt;Unit&gt;                  [m/s2]                                        4.0   &lt;Unit of acceleration&gt; Metre / Meter per square second (m=1, s=1)<br>
 *   430. [km/h2]             &lt;Unit&gt;                  [km/h2]                                       4.0   &lt;Unit of acceleration&gt; Kilometre / Kilometer per square hour (m=1, s=1)<br>
 *   431. [mi/h2]             &lt;Unit&gt;                  [mi/h2]                                       4.0   &lt;Unit of acceleration&gt; Mile per square hour (m=1, s=1)<br>
 *   432. [rad]               &lt;Unit&gt;                  [rad]                                         4.0   &lt;Unit of angle&gt; Radian (rad=1)<br>
 *   433. [deg]               &lt;Unit&gt;                  [deg]                                         4.0   &lt;Unit of angle&gt; Degree of arc (rad=1)<br>
 *   434. [']                 &lt;Unit&gt;                  [']                                           4.0   &lt;Unit of angle&gt; Minute of arc (rad=1)<br>
 *   435. ['']                &lt;Unit&gt;                  ['']                                          4.0   &lt;Unit of angle&gt; Second of arc (rad=1)<br>
 *   436. (                   &lt;Parser Symbol&gt;         ( ... )                                       1.0   Left parentheses<br>
 *   437. )                   &lt;Parser Symbol&gt;         ( ... )                                       1.0   Right parentheses<br>
 *   438. ,                   &lt;Parser Symbol&gt;         (a1, ... ,an)                                 1.0   Comma (function parameters)<br>
 *   439. ;                   &lt;Parser Symbol&gt;         (a1; ... ;an)                                 1.0   Semicolon (function parameters)<br>
 * <br><br>
 <!-- globalinfo-end -->
 *
 <!-- options-start -->
 * Valid options are: <p>
 *
 * <pre> -expression &lt;expression&gt;
 *  The expression to use.
 *  (default: 1)</pre>
 *
 * <pre> -use-attribute-names
 *  If enabled, attribute names instead of 'attX' are used in the expression.
 *  (default: disabled)</pre>
 *
 * <pre> -output-debug-info
 *  If set, classifier is run in debug mode and
 *  may output additional info to the console</pre>
 *
 * <pre> -do-not-check-capabilities
 *  If set, classifier capabilities are not checked before classifier is built
 *  (use with caution).</pre>
 *
 * <pre> -num-decimal-places
 *  The number of decimal places for the output of numbers in the model (default 2).</pre>
 *
 * <pre> -batch-size
 *  The desired batch size for batch prediction  (default 100).</pre>
 *
 <!-- options-end -->
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class MXExpression
  extends AbstractClassifier {

  private static final long serialVersionUID = -3849288591385551460L;

  /** the flag for the expression. */
  public final static String EXPRESSION = "expression";

  /** the flag for whether to use attribute names in the expression. */
  public final static String USE_ATTRIBUTE_NAMES = "use-attribute-names";

  /** the expression to use. */
  protected String m_Expression = getDefaultExpression();

  /** whether to use attribute names instead of attX. */
  protected boolean m_UseAttributeNames = getDefaultUseAttributeNames();

  /** the att index/argument name relation. */
  protected Map<Integer,String> m_ArgumentNames;

  /**
   * Returns a string describing this classifier.
   *
   * @return a description of the classifier suitable for
   * displaying in the explorer/experimenter gui
   */
  public String globalInfo() {
    return "Applies the specified expression to make a prediction.\n"
      + "All numeric attribute values are accessible via 'attX' (with X being "
      + " the 1-based index of the attribute) or their attribute name (lower "
      + "case, all non-alphanumeric characters removed).\n\n"
      + "Expression help:\n"
      + mXparser.getHelp();
  }

  /**
   * Returns an enumeration describing the available options.
   * 
   * @return an enumeration of all the available options.
   */
  @Override
  public Enumeration<Option> listOptions() {
    Vector<Option> result = new Vector<Option>();

    result.addElement(new Option(
      "\tThe expression to use.\n"
        + "\t(default: " + getDefaultExpression() + ")",
      EXPRESSION, 1, "-" + EXPRESSION + " <expression>"));

    result.addElement(new Option(
      "\tIf enabled, attribute names instead of 'attX' are used in the expression.\n"
        + "\t(default: disabled)",
      USE_ATTRIBUTE_NAMES, 0, "-" + USE_ATTRIBUTE_NAMES));

    result.addAll(Collections.list(super.listOptions()));

    return result.elements();
  }

  /**
   * Gets the current settings of the filter.
   * 
   * @return an array of strings suitable for passing to setOptions
   */
  @Override
  public String[] getOptions() {
    List<String> result = new ArrayList<String>();
    
    result.add("-" + EXPRESSION);
    result.add(getExpression());

    if (getUseAttributeNames())
      result.add("-" + USE_ATTRIBUTE_NAMES);

    Collections.addAll(result, super.getOptions());

    return result.toArray(new String[result.size()]);
  }

  /**
   * Parses a given list of options.
   *
   * @param options the list of options as an array of strings
   * @throws Exception if an option is not supported
   */
  @Override
  public void setOptions(String[] options) throws Exception {
    String 	tmpStr;

    tmpStr = Utils.getOption(EXPRESSION, options);
    if (!tmpStr.isEmpty())
      setExpression(tmpStr);
    else
      setExpression(getDefaultExpression());
    setUseAttributeNames(Utils.getFlag(USE_ATTRIBUTE_NAMES, options));

    super.setOptions(options);

    Utils.checkForRemainingOptions(options);
  }

  /**
   * Returns the default expression.
   *
   * @return the default
   */
  protected String getDefaultExpression() {
    return "1";
  }

  /**
   * Sets the expression to use.
   * 
   * @param value the expression
   */
  public void setExpression(String value) {
    m_Expression = value;
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
   * Returns the tip text for this property
   * 
   * @return tip text for this property suitable for displaying in the
   *         explorer/experimenter gui
   */
  public String expressionTipText() {
    return "The expression to use for making predictions.";
  }

  /**
   * Returns the default for using attribute names.
   *
   * @return the default
   */
  protected boolean getDefaultUseAttributeNames() {
    return false;
  }

  /**
   * Sets whether to use attribute names instead of indices ('attX').
   *
   * @param value true if to use attribute names
   */
  public void setUseAttributeNames(boolean value) {
    m_UseAttributeNames = value;
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
   * Returns the tip text for this property
   *
   * @return tip text for this property suitable for displaying in the
   *         explorer/experimenter gui
   */
  public String useAttributeNamesTipText() {
    return "If enabled, attribute names are used instead of 'attX' (lower-case, non-alphanumeric characters stripped).";
  }

  /**
   * Returns the Capabilities of this filter.
   *
   * @return the capabilities of this object
   * @see Capabilities
   */
  @Override
  public Capabilities getCapabilities() {
    Capabilities	result;

    result = new Capabilities(this);
    result.enable(Capability.NUMERIC_CLASS);
    result.enable(Capability.DATE_CLASS);
    result.enable(Capability.MISSING_CLASS_VALUES);
    result.enable(Capability.NUMERIC_ATTRIBUTES);
    result.enable(Capability.DATE_ATTRIBUTES);
    result.setMinimumNumberInstances(1);

    return result;
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
   * Builds the expression using the provided instance.
   *
   * @param instance	the instance to use
   * @return		the generated expression
   */
  protected Expression buildExpression(Instance instance) {
    List<Argument>	args;
    Expression 		result;
    int			i;

    args = new ArrayList<Argument>();
    for (i = 0; i < instance.numAttributes(); i++) {
      if (instance.attribute(i).isNumeric())
	args.add(new Argument(buildName(instance, i) + " = " + instance.value(i)));
    }
    result = new Expression(m_Expression, args.toArray(new Argument[args.size()]));

    return result;
  }

  /**
   * Generates a classifier.
   *
   * @param data set of instances serving as training data
   * @throws Exception if the classifier has not been
   * generated successfully
   */
  @Override
  public void buildClassifier(Instances data) throws Exception {
    Expression 		expr;

    getCapabilities().testWithFail(data);

    // reset names
    m_ArgumentNames = new HashMap<Integer, String>();

    // test expression
    if (data.numInstances() > 0) {
      expr = buildExpression(data.instance(0));
      if (!expr.checkSyntax())
        throw new WekaException(
          "Illegal expression syntax: " + m_Expression + "\n"
	    + expr.getErrorMessage());
    }
  }

  /**
   * Classifies the given test instance.
   *
   * @param instance the instance to be classified
   * @return the predicted most likely class for the instance or
   *         Utils.missingValue() if no prediction is made
   * @throws Exception if an error occurred during the prediction
   */
  @Override
  public double classifyInstance(Instance instance) throws Exception {
    Expression 		expr;

    expr = buildExpression(instance);
    return expr.calculate();
  }

  /**
   * Returns the underlying expression.
   *
   * @return the expression
   */
  @Override
  public String toString() {
    return "Expression: " + m_Expression;
  }

  /**
   * Returns the revision string.
   *
   * @return		the revision
   */
  @Override
  public String getRevision() {
    return RevisionUtils.extract("$Revision: 1 $");
  }

  /**
   * Main method for running this classifier from commandline.
   *
   * @param args 	the options
   */
  public static void main(String[] args) {
    runClassifier(new MXExpression(), args);
  }
}
