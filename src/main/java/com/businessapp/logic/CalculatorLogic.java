package com.businessapp.logic;

import com.businessapp.Component;
import com.businessapp.ControllerIntf;
import com.businessapp.fxgui.CalculatorGUI_Intf;
import com.businessapp.fxgui.CalculatorGUI_Intf.Token;

import java.util.ArrayList;


/**
 * Implementation of CalculatorLogicIntf that only displays Tokens
 * received from the Calculator UI.
 *
 */
class CalculatorLogic implements CalculatorLogicIntf {
	private CalculatorGUI_Intf view;
	private StringBuffer dsb = new StringBuffer();
	private final double VAT_RATE = 19.0;

	private ArrayList<Token> operators = new ArrayList<>();
	private ArrayList<Double> numbers = new ArrayList<>();
	private ArrayList<Integer> bracketsPos = new ArrayList<>();

	CalculatorLogic() {
	}

	@Override
	public void inject( ControllerIntf dep ) {
		this.view = (CalculatorGUI_Intf)dep;
	}

	@Override
	public void inject( Component parent ) {		
	}

	@Override
	public void start() {
		nextToken( Token.K_C );		// reset calculator
	}

	@Override
	public void stop() {
	}


	/**
     * Process next token received from UI controller.
     * <p>
     * Tokens are transformed into output into UI properties:
     * 	- CalculatorIntf.DISPLAY for numbers and
     * 	- CalculatorIntf.SIDEAREA for VAT calculations.
     * <p>
     * @param tok the next Token passed from the UI, CalculatorViewController.
     */
	public void nextToken( Token tok ) {
		String d = tok == Token.K_DOT ? "." : CalculatorGUI_Intf.KeyLabels[tok.ordinal()];
		try {
			switch (tok) {
				case K_0:
				case K_1:
				case K_2:
				case K_3:
				case K_4:
				case K_5:
				case K_6:
				case K_7:
				case K_8:
				case K_9:
					// if key numeric key pressed after 0 remove 0
					if (dsb.length() == 1) {
						if (dsb.lastIndexOf("0") == 0)
							dsb.setLength(0);
					}
					// checks if last operation was equals, if yes clear
					if (operators.size() > 0) {
						if (operators.get(operators.size() - 1) == Token.K_EQ) {
							operators.clear();
							numbers.clear();
						}
					}
					appendBuffer(d);
					break;

				case K_1000:
					nextToken(Token.K_0);
					nextToken(Token.K_0);
					nextToken(Token.K_0);
					break;

				case K_DIV:
				case K_MUL:
				case K_PLUS:
				case K_MIN:
				case K_EQ:
					// if no input, when first operator was clicked append 0
					if (numbers.size() == 0 && dsb.length() == 0)
						appendBuffer("0");
					// checks if last operation is equals, if yes put the result in
					// the StringBuffer and clear
					if (operators.size() > 0 && operators.get(operators.size() - 1) == Token.K_EQ) {
						operators.remove(operators.size() - 1);
						dsb.append(numbers.get(0));
						numbers.clear();
					}
					// change last operator, if two operators were clicked
					// successively
					if (dsb.length() == 0 && operators.size() == numbers.size())
						operators.set(operators.size() - 1, tok);
					else {
						operators.add(tok);
						if (dsb.length() != 0)
							numbers.add(Double.parseDouble(dsb.toString()));
						dsb.setLength(0);
						calc();
					}
					break;

				case K_VAT:
					double current = 0;
					if (dsb.length() == 0 && numbers.size() != 0) {
						current = numbers.get(numbers.size() - 1);
					} else if (dsb.length() != 0) {
						current = Double.parseDouble(dsb.toString());
					}
					double vat = calcVat(current);
					view.writeSideArea(
							"Brutto:  " + current + "\n" + VAT_RATE + "% MwSt:  " + vat + "\n" + "Netto:  " + (current - vat));
					break;

				case K_DOT:
					if (numbers.size() == 0 && dsb.length() == 0)
						appendBuffer("0.");
					else if (dsb.length() > 0) {
						if (dsb.lastIndexOf(".") == -1)
							appendBuffer(d);
					}
					break;

				case K_BACK:
					dsb.setLength(Math.max(0, dsb.length() - 1));
					break;

				case K_C:
					view.writeSideArea("");
					dsb.setLength(0);
					break;
				case K_CE:
					dsb.setLength(0);
					view.writeSideArea("");
					operators.clear();
					numbers.clear();
					bracketsPos.clear();
					break;

				default:
			}
			if (dsb.length() == 0) {
				if (numbers.size() != 0) {
					view.writeTextArea(numbers.get(numbers.size() - 1).toString());
				} else
					view.writeTextArea("0");
			} else
				view.writeTextArea(dsb.toString());
			System.out.println(dsb);
			System.out.println("operators: " + operators);
			System.out.println("numbers: " + numbers);
			System.out.println("brackets: " + bracketsPos);
			System.out.println("buffered String: " + dsb.toString());

		} catch( ArithmeticException e ) {
			view.writeTextArea( e.getMessage() );
		}
	}

	/*
	 * Private method(s).
	 */
	private void appendBuffer( String d ) {
		if( dsb.length() <= CalculatorGUI_Intf.DISPLAY_MAXDIGITS ) {
			dsb.append( d );
		}
	}

	private double calcVat(double number) {
		double net = number / (1 + VAT_RATE / 100);
		return round(number - net);
	}

	private double round(double number) {
		number = Math.round(number * 100);
		return number / 100;

	}

	private void calc() {
		int bracketsOffset = 0;
		int lastBracket = 0;
		// set bracketsOffset
		if (!bracketsPos.isEmpty()) {
			lastBracket = bracketsPos.get(bracketsPos.size() - 1);
			// if last bracket is closing bracket (-1) take offset of previous bracket
			if (lastBracket == -1) {
				bracketsOffset = bracketsPos.get(bracketsPos.size() - 2);
			} else // else offset = lastBracket value (position of bracket, not index)
				bracketsOffset = lastBracket;
		}
		// if closed bracket with one operator (x+y) *,/ before +,-
		if (operators.size() - bracketsOffset == 1 && lastBracket == -1) {
			double firstNum = numbers.get(0 + bracketsOffset);
			double secondNum = numbers.get(1 + bracketsOffset);
			Token firstOperator = operators.get(0 + bracketsOffset);
			Double newFirstNum = null;
			if (firstOperator == Token.K_MUL)
				newFirstNum = round(firstNum * secondNum);
			else if (firstOperator == Token.K_DIV) {
				if (secondNum == 0) {
					nextToken(Token.K_CE);
					throw new ArithmeticException("ERR: div by zero");
				}
				newFirstNum = round(firstNum / secondNum);
			} else if (firstOperator == Token.K_PLUS)
				newFirstNum = round(firstNum + secondNum);
			else if (firstOperator == Token.K_MIN)
				newFirstNum = round(firstNum - secondNum);
			operators.remove(0 + bracketsOffset);
			numbers.set(0 + bracketsOffset, newFirstNum);
			numbers.remove(1 + bracketsOffset);
			bracketsPos.remove(bracketsPos.size() - 1);
			bracketsPos.remove(bracketsPos.size() - 1);
		}

		// if more than 1 operator ...
		else if (operators.size() - bracketsOffset > 1) {
			double firstNum = numbers.get(0 + bracketsOffset);
			double secondNum = numbers.get(1 + bracketsOffset);
			Token firstOperator = operators.get(0 + bracketsOffset);
			Token secondOperator = operators.get(1 + bracketsOffset);
			Token lastOperator = operators.get(operators.size() - 1);

			// if 2 operators and no closing bracket
			if (operators.size() - bracketsOffset == 2 && lastBracket != -1) {
				// if first operator *,/ calculate first operation
				if (firstOperator == Token.K_MUL || firstOperator == Token.K_DIV) {
					Double newFirstNum = null;
					if (firstOperator == Token.K_MUL)
						newFirstNum = round(firstNum * secondNum);
					else if (firstOperator == Token.K_DIV) {
						if (secondNum == 0) {
							nextToken(Token.K_CE);
							throw new ArithmeticException("ERR: div by zero");
						}
						newFirstNum = round(firstNum / secondNum);
					}
					operators.remove(0 + bracketsOffset);
					numbers.set(0 + bracketsOffset, newFirstNum);
					numbers.remove(1 + bracketsOffset);
				}

				// else (first operator +,-) if second operator +,- calculate first operation, else do nothing, wait...
				else if (secondOperator == Token.K_PLUS || secondOperator == Token.K_MIN
						|| lastOperator == Token.K_EQ) {
					Double newFirstNum = null;
					if (firstOperator == Token.K_PLUS)
						newFirstNum = round(firstNum + secondNum);
					else if (firstOperator == Token.K_MIN)
						newFirstNum = round(firstNum - secondNum);
					operators.remove(0 + bracketsOffset);
					numbers.set(0 + bracketsOffset, newFirstNum);
					numbers.remove(1 + bracketsOffset);
				}
			}

			// if 3 operators or 2 and a closing bracket...
			else if (operators.size() - bracketsOffset == 3
					|| (operators.size() - bracketsOffset == 2 && lastBracket == -1)) {
				double thirdNum = numbers.get(2 + bracketsOffset);
				// if second operator *,/ calculate second operation
				if (secondOperator == Token.K_MUL || secondOperator == Token.K_DIV || lastOperator == Token.K_EQ) {
					Double newLastNum = null;
					if (secondOperator == Token.K_MUL)
						newLastNum = round(secondNum * thirdNum);
					else if (secondOperator == Token.K_DIV) {
						if (thirdNum == 0) {
							nextToken(Token.K_CE);
							throw new ArithmeticException("ERR: div by zero");
						}
						newLastNum = round(secondNum / thirdNum);
						// else (second operator +,-) calculate first operation
					} else if (firstOperator == Token.K_PLUS)
						newLastNum = round(firstNum + secondNum);
					else if (firstOperator == Token.K_MIN)
						newLastNum = round(firstNum - secondNum);
					operators.remove(1 + bracketsOffset);
					numbers.set(1 + bracketsOffset, newLastNum);
					numbers.remove(2 + bracketsOffset);
					calc();
				}
			}
		}
	}

}
