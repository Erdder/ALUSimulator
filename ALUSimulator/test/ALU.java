
import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import java.util.ArrayList;

/**
 * 模拟ALU进行整数和浮点数的四则运算
 */

public class ALU {

	/**
	 * 1
	 * 生成十进制整数的二进制补码表示。<br/>
	 * 例：integerRepresentation("9", 8)
	 *
	 * @param number 十进制整数。若为负数；则第一位为“-”；若为正数或 0，则无符号位
	 * @param length 二进制补码表示的长度
	 * @return number的二进制补码表示，长度为length
	 */
	public String integerRepresentation(String number, int length) {
		// TODO YOUR CODE HERE.
		int opNumber = Integer.parseInt(number);
		ArrayList<Integer> binaryCode = new ArrayList<>();
		String result = "";
		int lengthTemp = length;
		while (lengthTemp != 0) {
			binaryCode.add(opNumber & 1);   //未解之谜 也没取反啊
			opNumber = opNumber >> 1;
			System.out.println(opNumber);
			lengthTemp--;
		}
		for (int j = 0; j < binaryCode.size(); j++) {
			result = result + binaryCode.get(length - 1 - j);
		}
		return result;
	}

	public String smallFloatRepresetation(String number, int length) {
	    /*
	    将小于0的小数转化为二进制，不包含小数点及前面的0
	     */
		double opNumber = Double.parseDouble(number);
		String result = "";
		while (!number.substring(number.indexOf('.') + 1).equals("0")) {
			opNumber = opNumber * 2;																					//乘2一直到小数点后面为0  如果是2.02这种格式呢。。。  哦哦 是小数点后面的子字符串全是0
			number = opNumber + "";                                                                                     //转化为字符串形式
			if (number.substring(0, 1).equals("1")) {
				result += "1";
				opNumber = opNumber - 1;
			} else {
				result += "0";
			}
		}
		return result;
	}


	/**
	 * 生成十进制浮点数的二进制表示。 需要考虑 0、反规格化、正负无穷（“+Inf”和“-Inf”）、 NaN等因素，具体借鉴 IEEE 754。
	 * 舍入策略为向0舍入。<br/>
	 * 例：floatRepresentation("11.375", 8, 11)
	 *
	 * @param number  十进制浮点数，包含小数点。若为负数；则第一位为“-”；若为正数或 0，则无符号位
	 * @param eLength 指数的长度，取值大于等于 4
	 * @param sLength 尾数的长度，取值大于等于 4
	 * @return number的二进制表示，长度为 1+eLength+sLength。从左向右，依次为符号、指数（移码表示）、尾数（首位隐藏）
	 */
	public String floatRepresentation(String number, int eLength, int sLength) {
		// TODO YOUR CODE HERE.
        /*
        判断是否是0，反规格化，正负无穷，NaN
        计算整数部分的二进制，计算小数部分的二进制
        连接起来移小数点，判断指数
         */
		boolean isNegative = false;
		boolean isInteger = false;
		String symbol = "0";																							//符号位
		if (number.charAt(0) == '-') {
			isNegative = true;
			number = number.substring(1);
			symbol = "1";
		}
		int count = 0;                              																    //判断有没有小数点和找出小数点的位置
		for (int i = 0; i < number.length(); i++) {
			if (!(number.charAt(i) == '.')) {
				count++;
			}
		}
		if (count == number.length()) {
			number = number + ".0";
			isInteger = true;                //是个整数
		}
		String integerPart = number.substring(0, number.indexOf('.'));                   //整数部分
		String fraction = number.substring(number.indexOf('.'));                      //小数部分(包含小数点，去掉报错越界
		System.out.println(fraction);
		String integerBinary = integerRepresentation(integerPart, sLength);            //得到整数部分的补码表示
		System.out.println(integerBinary);
		String fractionBinary = smallFloatRepresetation(fraction, sLength);            //得到小数部分的补码表示
		System.out.println(fractionBinary);


		String originalResult = integerBinary + "." + fractionBinary;                       //还没有开始移位的一串
		int countOfMove = 0;
		int index = originalResult.indexOf('.');
		while (originalResult.charAt(index - 1) != '1') {
			originalResult = originalResult.substring(0, index - 2) + "." + originalResult.substring(index - 1, index) + originalResult.substring(index);
			index = originalResult.indexOf('.');
			countOfMove++;
		}
		String exponentStr = integerRepresentation(countOfMove + "", eLength);
		String pianyi = "";
		if (eLength == 8)
			pianyi = "1111111";
		else if (eLength == 11) {
			pianyi = "1111111111";
		}
		String exponent = integerAddition(exponentStr, pianyi, eLength);
		for (int i = fractionBinary.length(); i < sLength; i++) {
			fractionBinary += "0";
		}
		String retResult = symbol + exponent + fractionBinary;
		return retResult;
	}

	/**
	 * 生成十进制浮点数的IEEE 754表示，要求调用{@link #floatRepresentation(String, int, int)
	 * floatRepresentation}实现。<br/>
	 * 例：ieee754("11.375", 32)
	 *
	 * @param number 十进制浮点数，包含小数点。若为负数；则第一位为“-”；若为正数或 0，则无符号位
	 * @param length 二进制表示的长度，为32或64
	 * @return number的IEEE 754表示，长度为length。从左向右，依次为符号、指数（移码表示）、尾数（首位隐藏）
	 */
	public String ieee754(String number, int length) {
		// TODO YOUR CODE HERE.
		return "";
	}

	/**
	 * 4
	 * 计算二进制补码表示的整数的真值。<br/>
	 * 例：integerTrueValue("00001001")
	 *
	 * @param operand 二进制补码表示的操作数
	 * @return operand的真值。若为负数；则第一位为“-”；若为正数或 0，则无符号位
	 */
	public String integerTrueValue(String operand) {
		// TODO YOUR CODE HERE.
		String originalCode;
		String retStr = "";
		if (operand.charAt(0) == '0') {                   //如果第一位是0，正数，补码与原码相同			划重点！‘0’
			originalCode = operand;
			retStr = "" + binaryToInteger(originalCode);
		} else {                                           //如果是1，负数，补码取补码得到原码
			originalCode = negation(operand);
			originalCode = oneAdder(originalCode);
			int retNum = binaryToInteger(originalCode.substring(1));
			if (retNum == 0)                    //如果是0，不要返回-0.   1000000是0
				retStr = "" + retNum;
			else
				retStr = "-" + retNum;
		}
		return retStr;
	}

	public int binaryToInteger(String operand) {
		/**
		 * 将二进制原码转化成十进制
		 * 默认进来的都是正数，不需要考虑符号位
		 * 所以返回int类型
		 */
		int length = operand.length();
		int num = 0;
		int ret = 0;
		for (int i = length - 1; i >= 0; i--) {
			num = Integer.parseInt(operand.substring(i, i + 1));            //i位上的数字
			for (int counter = 0; counter < length - 1 - i; counter++) {
				num = num * 2;                                                //ret*2^counter次方
			}
			ret += num;
		}
		return ret;
	}

	/**
	 * 计算二进制原码表示的浮点数的真值。<br/>
	 * 例：floatTrueValue("01000001001101100000", 8, 11)
	 *
	 * @param operand 二进制表示的操作数
	 * @param eLength 指数的长度，取值大于等于 4
	 * @param sLength 尾数的长度，取值大于等于 4
	 * @return operand的真值。若为负数；则第一位为“-”；若为正数或 0，则无符号位。正负无穷分别表示为“+Inf”和“-Inf”，
	 * NaN表示为“NaN”
	 */
	public String floatTrueValue(String operand, int eLength, int sLength) {
		String symbol = "";
		if (operand.substring(0, 1).equals("1")) {
			symbol = "-";
		}
		//切割
		String pianyi = "0";
		for (int i = 0; i < eLength - 1; i++) {
			pianyi += "1";
		}
		String exponent = operand.substring(1, eLength + 1);
		pianyi = oneAdder(negation(pianyi)).substring(1);
		exponent = integerAddition(exponent, pianyi, eLength).substring(1);
		String fraction = operand.substring(eLength + 1);

		//这里得到了exponent是1111，但是怎么得到-1呢。。。
		String expStr = oneAdder(negation(exponent));//得到了补码
		System.out.println(binaryToInteger(expStr));
		double integerPart = Math.pow(2, binaryToInteger(expStr));
		double fractionPart = fracBinaToInteger(fraction);
		double result = integerPart + fractionPart;
		String retStr = symbol + result;
		return retStr;
	}

	public double fracBinaToInteger(String operand) {
	    /*
	    将一串二进制的小数部分求值，注意只是传入小数点以后的部分
	     */
		double result = 0;
		int count = -1;
		for (int i = 0; i < operand.length(); i++) {
			result += Math.pow(2, count) * Integer.parseInt(operand.substring(i, i + 1));
			count--;
		}
		return result;
	}


	/**
	 * 5
	 * 按位取反操作。<br/>
	 * 例：negation("00001001")
	 *
	 * @param operand 二进制表示的操作数
	 * @return operand按位取反的结果
	 */
	public String negation(String operand) {
		// TODO YOUR CODE HERE.
		int num;
		String retStr = "";
		for (int i = 0; i < operand.length(); i++) {
			num = Integer.parseInt(operand.substring(i, i + 1));
			num = num ^ 1;                                //异或相同为0，相异为1
			retStr = retStr + "" + num;
		}
		return retStr;
	}

	/**
	 * 左移操作。<br/>
	 * 例：leftShift("00001001", 2)
	 *
	 * @param operand 二进制表示的操作数
	 * @param n       左移的位数
	 * @return operand左移n位的结果
	 */
	public String leftShift(String operand, int n) {
		// TODO YOUR CODE HERE.
		int length = operand.length();
		for (int i = 0; i < n; i++) {
			operand = operand.substring(1, length) + "0";					//左移就是去掉左边的，右边直接补0
		}
		return operand;
	}

	/**
	 * 逻辑右移操作。<br/>
	 * 逻辑右移 不考虑符号位 直接左边加0
	 * 例：logRightShift("11110110", 2)
	 *
	 * @param operand 二进制表示的操作数
	 * @param n       右移的位数
	 * @return operand逻辑右移n位的结果
	 */
	public String logRightShift(String operand, int n) {
		// TODO YOUR CODE HERE.
		int length = operand.length();
		for (int i = 0; i < n; i++) {
			operand = "0" + operand.substring(0, length - 1);				//逻辑右移就是去掉右边的，左边加0
		}
		return operand;
	}

	/**
	 * 算术右移操作。<br/>
	 * 例：ariRightShift("11110110", 2)
	 *
	 * @param operand 二进制表示的操作数
	 * @param n       右移的位数
	 * @return operand算术右移n位的结果
	 */
	public String ariRightShift(String operand, int n) {
		// TODO YOUR CODE HERE.
		int length = operand.length();
		String firstCha = operand.substring(0, 1);					//算术右移就是左边补符号位
		for (int i = 0; i < n; i++) {
			operand = firstCha + operand.substring(0, length - 1);
		}
		return operand;
	}

	/**
	 * 全加器，对两位以及进位进行加法运算。<br/>
	 * 例：fullAdder('1', '1', '0')
	 *
	 * @param x 被加数的某一位，取0或1
	 * @param y 加数的某一位，取0或1
	 * @param c 低位对当前位的进位，取0或1
	 * @return 相加的结果，用长度为2的字符串表示，第1位表示进位，第2位表示和
	 */
	public String fullAdder(char x, char y, char c) {
		// TODO YOUR CODE HERE
		int xx = x - 48;
		int yy = y - 48;
		int cc = c - 48;
		int s = 0;                                                    //某一位运算结果
		s = xx ^ yy ^ cc;
		cc = (xx & cc) | (yy & cc) | (xx & yy);
		String sStr = s + "";
		String cStr = cc + "";
		String ret = cStr + sStr;
		return ret;
	}


	/**
	 * 4位先行进位加法器。要求采用{@link #fullAdder(char, char, char) fullAdder}来实现<br/>
	 * 例：claAdder("1001", "0001", '1')
	 *
	 * @param operand1 4位二进制表示的被加数
	 * @param operand2 4位二进制表示的加数
	 * @param c        低位对当前位的进位，取0或1
	 * @return 长度为5的字符串表示的计算结果，其中第1位是最高位进位，后4位是相加结果，其中进位不可以由循环获得
	 */
	public String claAdder(String operand1, String operand2, char c) {
		// TODO YOUR CODE HERE.
		char[] xes = operand1.toCharArray();
		char[] yes = operand2.toCharArray();
		ArrayList<Integer> P = new ArrayList<>();
		ArrayList<Integer> G = new ArrayList<>();
		ArrayList<Integer> S = new ArrayList<>();        //这个是xy做异或的结果
		int x, y, cc;
		int p, g, s;
		for (int i = 3; i >= 0; i--) {                         //转化为数组时，顺序是反的
			x = xes[i] - 48;
			y = yes[i] - 48;
			p = x | y;
			g = x & y;
			s = x ^ y;
			P.add(p);
			G.add(g);
			S.add(s);                                   //只有xy异或的结果
		}
		cc = c - 48;
		String s0 = (S.get(0) ^ cc) + "";
		cc = G.get(0) | (P.get(0) & cc);
		String s1 = (S.get(1) ^ cc) + "";
		cc = G.get(1) | (P.get(1) & G.get(0)) | (P.get(0) & P.get(1) & cc);
		String s2 = (S.get(2) ^ cc) + "";
		cc = G.get(2) | (P.get(2) & G.get(1)) | (P.get(2) & P.get(1) & G.get(0)) | (P.get(0) & P.get(1) & P.get(2) & cc);
		String s3 = (S.get(3) ^ cc) + "";
		cc = G.get(3) | (P.get(3) & G.get(2)) | (P.get(3) & P.get(2) & P.get(1) & G.get(0)) | (P.get(3) & P.get(0) & P.get(1) & P.get(2) & cc);
		String retStr = cc + s3 + s2 + s1 + s0;
		return retStr;
	}


	/**
	 * 12
	 * 加一器，实现操作数加1的运算。 需要采用与门、或门、异或门等模拟，
	 * 不可以直接调用{@link #fullAdder(char, char, char) fullAdder}、
	 * {@link #claAdder(String, String, char) claAdder}、
	 * {@link #adder(String, String, char, int) adder}、
	 * {@link #integerAddition(String, String, int) integerAddition}方法。<br/>
	 * 例：oneAdder("00001001")
	 *
	 * @param operand 二进制补码表示的操作数
	 * @return operand加1的结果，长度为operand的长度加1，其中第1位指示是否溢出（溢出为1，否则为0），其余位为相加结果
	 */
	public String oneAdder(String operand) {
		// TODO YOUR CODE HERE.
		int y = 1;
		int c = 0;
		int s;
		int flagSec = 0;
		String retStr = "";
		for (int i = operand.length() - 1; i >= 0; i--) {
			int x = Integer.parseInt(operand.substring(i, i + 1));                   //将x转化为int类型
			if (i == operand.length() - 1) {                                         //operand的最后一位，也就是运算的第一位，c0为0
				s = x ^ y;
				c = x & y;
			} else {
				s = x ^ y ^ c;
				c = (x & c) | (y & c) | (x & y);
				if (i == 1) {                                                             //如果进位跟运算结果相同，即为越位
																							//为啥是倒数第二位的进位和最后一位的运算结果相同就越位？？？？
					flagSec = c;
				}
			}
			y = 0;
			retStr = s + retStr;
		}
		if (c == flagSec)
			retStr = 0 + retStr;
		else
			retStr = 1 + retStr;
		return retStr;
	}

	/**
	 * 加法器，要求调用{@link #claAdder(String, String, char)}方法实现。<br/>
	 * 例：adder("0100", "0011", ‘0’, 8)
	 *
	 * @param operand1 二进制补码表示的被加数
	 * @param operand2 二进制补码表示的加数
	 * @param c        最低位进位
	 * @param length   存放操作数的寄存器的长度，为4的倍数。length不小于操作数的长度，当某个操作数的长度小于length时，需要在高位补符号位
	 * @return 长度为length+1的字符串表示的计算结果，其中第1位指示是否溢出（溢出为1，否则为0），后length位是相加结果
	 * 溢出分为cn！=cn-1  和  sn！=x且sn！=y
	 * 好像只要判断一下 sn和x和y的关系就可以判断溢出了
	 */
	public String adder(String operand1, String operand2, char c, int length) {
		// TODO YOUR CODE HERE.
        /*
        先符号扩展，再进行计算
        每四位调用一次claAdder()方法，每次的c都是前面一个四字组的最后一位进位
         */
		String symbolFir = operand1.substring(0, 1);
		String symbolSec = operand2.substring(0, 1);
		String tempResult = "";
		String result = "";

		//符号扩展到length长度位
		for (int i = operand1.length(); i < length; i++) {
			operand1 = symbolFir + operand1;
		}
		for (int i = operand2.length(); i < length; i++) {
			operand2 = symbolSec + operand2;
		}

		for (int i = length; i > 0; i = i - 4) {
			String opa = operand1.substring(i - 4, i);
			String opb = operand2.substring(i - 4, i);
			String temp = "";
			temp = claAdder(opa, opb, c);
			char[] cList = temp.toCharArray();                                                                          //每四位最后的进位
			c = cList[0];
			tempResult = tempResult + temp.substring(1);                                                                //结果相连，去掉cla返回值里的符号位
		}

		for (int i = length; i > 0; i = i - 4) {																		//将刚刚得到的倒序
			result = result + tempResult.substring(i - 4, i);                                                           //这个为什么要单独写勒？？？因为长度可能没到 不单独写会溢出
		}

		String symbolOfOverflow = "0";
		if (symbolFir.equals(symbolSec)) {                                                                              //判断溢出，如果xy相等且s不等于xy，则溢出；否则都不是溢出
			if (result.substring(0, 1).equals(symbolFir)) {
				symbolOfOverflow = "0";
			} else {
				symbolOfOverflow = "1";
			}
		}
		return symbolOfOverflow + result;

	}


	/**
	 * 整数加法，要求调用{@link #adder(String, String, char, int) adder}方法实现。<br/>
	 * 例：integerAddition("0100", "0011", 8)
	 *
	 * @param operand1 二进制补码表示的被加数
	 * @param operand2 二进制补码表示的加数
	 * @param length   存放操作数的寄存器的长度，为4的倍数。length不小于操作数的长度，当某个操作数的长度小于length时，需要在高位补符号位
	 * @return 长度为length+1的字符串表示的计算结果，其中第1位指示是否溢出（溢出为1，否则为0），后length位是相加结果
	 */
	public String integerAddition(String operand1, String operand2, int length) {
		// TODO YOUR CODE HERE.
		String result = adder(operand1, operand2, '0', length);
		return result;
	}

	/**
	 * 整数减法，可调用{@link #adder(String, String, char, int) adder}方法实现。<br/>
	 * 例：integerSubtraction("0100", "0011", 8)
	 *
	 * @param operand1 二进制补码表示的被减数
	 * @param operand2 二进制补码表示的减数
	 * @param length   存放操作数的寄存器的长度，为4的倍数。length不小于操作数的长度，当某个操作数的长度小于length时，需要在高位补符号位
	 * @return 长度为length+1的字符串表示的计算结果，其中第1位指示是否溢出（溢出为1，否则为0），后length位是相减结果
	 */
	public String integerSubtraction(String operand1, String operand2, int length) {
		// TODO YOUR CODE HERE.
        /*
        减数取负，取反加一，再调用adder方法
         */
		String temp = "";
		temp = negation(operand2);
		operand2 = oneAdder(temp);
		String result = adder(operand1, operand2.substring(1), '0', length);
		return result;
	}

	/**
	 * 整数乘法，使用Booth算法实现，可调用{@link #adder(String, String, char, int)
	 * adder}等方法。<br/>
	 * 例：integerMultiplication("0100", "0011", 8)
	 *
	 * @param operand1 二进制补码表示的被乘数
	 * @param operand2 二进制补码表示的乘数
	 * @param length   存放操作数的寄存器的长度，为4的倍数。length不小于操作数的长度，当某个操作数的长度小于length时，需要在高位补符号位
	 * @return 长度为length+1的字符串表示的相乘结果，其中第1位指示是否溢出（溢出为1，否则为0），后length位是相乘结果
	 */
	public String integerMultiplication(String operand1, String operand2, int length) {
		// TODO YOUR CODE HERE.
        /*
          operand2是乘数，和A一起放每次算的结果
          operand1是被乘数，每次不变就完事儿了
          product是关于operand1的，也就是A 是部分和的结果
          Q0是operand2的最右边一位
          QSymbol是书上205页中的Q-1，初始化为0，是在Q0的再右边，用来右移的吧。。。 一个符号位，最后计算结果的时候不考虑在内
         */
		int QSymbol;
		int oriLength = operand1.length();

		for (int i = oriLength; i < length; i++) {
			operand1 = operand1.substring(0, 1) + operand1;
		}
		for (int i = operand2.length(); i < length; i++) {
			operand2 = operand2.substring(0, 1) + operand2;
		}                                                                                                               //扩展符号位
		String q0 = "0";                                                                                                //乘数的最后一位 符号位
		String oriZero = "";
		for (int i = 0; i < length; i++) {
			oriZero += "0";
		}
		String result = oriZero + operand2 + q0;                                                                            //就是ppt里九位的那一行,初始化为前后都加0
		String product;

		for (int i = length * 2; i > length; i--) {
			product = result.substring(0, length);                                                                      //product为前length位
			q0 = result.substring(length * 2 - 1, length * 2);
			int Q0 = Integer.parseInt(q0);                                                                              //Q的最后一位,也就是整个result的倒数第二位
			QSymbol = result.charAt(length * 2) - 48;                                                                   //符号位
			if (QSymbol - Q0 < 0) {
				product = integerSubtraction(product, operand1, length).substring(1);                                    //-x
			} else if (QSymbol - Q0 > 0) {
				product = integerAddition(product, operand1, length).substring(1);                                        //+x
			}                                                                                                           //如果等于0，product是不变的
			result = product + result.substring(length);
			result = ariRightShift(result, 1);                                                                       //右移一位，算术右移（看符号的
		}
		String retStr = result.substring(0, result.length() - 1);                                                       //去掉符号位的2*length位运算结果
		String symbolStr = retStr.substring(0, length);                                                                  //前面length位符号补充位
		retStr = retStr.substring(length);                                                                              //单纯的运算结果，应该为length长度
		String flag = "0";                                                                                              //判断溢出位
		loop:
		for (int i = 0; i < symbolStr.length(); i++) {
			if (symbolStr.charAt(i) != retStr.charAt(0)) {			//如果前面的符号位跟运算结果的第一位不同  就溢出了
				flag = "1";
				break loop;
			}
		}
		retStr = flag + retStr;
		return retStr;
	}

	/**
	 * 整数的不恢复余数除法，可调用{@link #adder(String, String, char, int) adder}等方法实现。<br/>
	 * 例：integerDivision("0100", "0011", 8)
	 *
	 * @param operand1 二进制补码表示的被除数
	 * @param operand2 二进制补码表示的除数
	 * @param length   存放操作数的寄存器的长度，为4的倍数。length不小于操作数的长度，当某个操作数的长度小于length时，需要在高位补符号位
	 * @return 长度为2*length+1的字符串表示的相除结果，其中第1位指示是否溢出（溢出为1，否则为0），其后length位为商，最后length位为余数
	 */
	public String integerDivision(String operand1, String operand2, int length) {
		// TODO YOUR CODE HERE.
		try {
			String retStr = "";
			int x = Integer.parseInt(operand1);
			int y = Integer.parseInt(operand2);
			if (x == 0 & y != 0) {
				retStr = "0";
			} else if (x == 0 & y == 0) {
				retStr = "NaN";
			} else if (x != 0 & y != 0) {
				for (int i = operand1.length(); i < length; i++) {
					operand1 = operand1.substring(0, 1) + operand1;
				}
				for (int i = operand2.length(); i < length; i++) {
					operand2 = operand2.substring(0, 1) + operand2;
				}                                                                                                       //补全扩展位
				String remainder = "";
				for (int i = 0; i < length; i++) {
					remainder += operand1.substring(0, 1);                                                               //初始化的remainder，是length位长的被除数的符号位
				}
				String quotient = operand1;                                                                             //初始化的quotient就是被除数
				String antiOperand2 = oneAdder(negation(operand2)).substring(1);                                        //负的除数
				String result;

				for (int i = 0; i <= length; i++) {
					boolean Flag = posiOrNeg(remainder, operand2);                                                      //先看被除数和除数的符号，如果相同就用被除数减除数，符号不同就相加
					//System.out.println(remainder + " " + quotient);
					if (Flag) {                                                                                         //第一次进行加减法，即补位跟除数比较符号
						remainder = adder(remainder, antiOperand2, '0', length).substring(1);                        //如果相同，就减法
					} else {
						remainder = adder(remainder, operand2, '0', length).substring(1);
					}
					//System.out.println(remainder);
					if (posiOrNeg(remainder, operand2)) {
						quotient = quotient + "1";
					} else {
						quotient = quotient + "0";
					}
					//System.out.println(quotient);
					result = remainder + quotient;
					//System.out.println(result+"*");
					if (i != length) {
						result = leftShift(result, 1).substring(0, length * 2);
					}
					remainder = result.substring(0, length);
					quotient = result.substring(length);
				}

				quotient = leftShift(quotient, 1).substring(0, length);                                               //恢复商
				if (!posiOrNeg(quotient, operand2)) {
					quotient = oneAdder(quotient).substring(1);             //调用adder函数为什么不对
				}
				if (!posiOrNeg(remainder, operand2)) {                                                                  //恢复余数
					remainder = adder(remainder, operand2, '0', length).substring(1);
				} else {
					remainder = adder(remainder, antiOperand2, '0', length).substring(1);
				}
				retStr = quotient + remainder;
			}
			return retStr;
		} catch (Exception E) {
			return "exception";
		}
	}


	public boolean posiOrNeg(String str1, String str2) {
	    /*
	    用来判断符号是否相同
	     */
		char a = str1.charAt(0);
		char b = str2.charAt(0);
		if (a == b)
			return true;
		else
			return false;
	}

	/**
	 * 带符号整数加法，可以调用{@link #adder(String, String, char, int) adder}等方法，
	 * 但不能直接将操作数转换为补码后使用{@link #integerAddition(String, String, int)
	 * integerAddition}、 {@link #integerSubtraction(String, String, int)
	 * integerSubtraction}来实现。<br/>
	 * 例：signedAddition("1100", "1011", 8)
	 *
	 * @param operand1 二进制原码表示的被加数，其中第1位为符号位
	 * @param operand2 二进制原码表示的加数，其中第1位为符号位
	 * @param length   存放操作数的寄存器的长度，为4的倍数。length不小于操作数的长度（不包含符号），当某个操作数的长度小于length时，需要将其长度扩展到length
	 * @return 长度为length+2的字符串表示的计算结果，其中第1位指示是否溢出（溢出为1，否则为0），第2位为符号位，后length位是相加结果
	 */
	public String signedAddition(String operand1, String operand2, int length) {
		// TODO YOUR CODE HERE.
		return "";
	}

	/**
	 * 浮点数加法，可调用{@link #signedAddition(String, String, int)
	 * signedAddition}等方法实现。<br/>
	 * 例：floatAddition("00111111010100000", "00111111001000000", 8, 8, 8)
	 *
	 * @param operand1 二进制表示的被加数
	 * @param operand2 二进制表示的加数
	 * @param eLength  指数的长度，取值大于等于 4
	 * @param sLength  尾数的长度，取值大于等于 4
	 * @param gLength  保护位的长度
	 * @return 长度为2+eLength+sLength的字符串表示的相加结果，其中第1位指示是否指数上溢（溢出为1，否则为0），其余位从左到右依次为符号、指数（移码表示）、尾数（首位隐藏）。舍入策略为向0舍入
	 */
	public String floatAddition(String operand1, String operand2, int eLength, int sLength, int gLength) {
		// TODO YOUR CODE HERE.
		if (Double.parseDouble(operand1.substring(1)) == 0.0 || Double.parseDouble(operand2.substring(1)) == 0.0) {
			if (Double.parseDouble(operand1) == 0.0) {
				return "0" + operand2;
			}
			if (Double.parseDouble(operand2) == 0.0) {
				return "0" + operand1;
			}
		}
		if (operand1.equals(operand2)) {
			return floatMultiplication(operand1, floatRepresentation("2", 4, 4), eLength, sLength);
		}
		// 截取符号位
		String symbol1 = operand1.substring(0, 1);
		String symbol2 = operand2.substring(0, 1);
		// 截取指数
		String e1 = operand1.substring(1, 1 + eLength);
		String e2 = operand2.substring(1, 1 + eLength);
		// 截取尾数并补隐藏位
		char hide1 = '1';// 
		char hide2 = '1';
		if (Integer.parseInt(integerTrueValue(e1)) == 0) {
			hide1 = '0';
		}
		if (Integer.parseInt(integerTrueValue(e2)) == 0) {
			hide2 = '0';
		}
		String sign1 = hide1 + operand1.substring(eLength + 1);
		String sign2 = hide2 + operand2.substring(eLength + 1);
		// 增加保护位
		for (int i = 0; i < gLength; i++) {
			sign1 += "0";
			sign2 += "0";
		}
		// 判断是否有加数等于0
		if (floatTrueValue(operand1, eLength, sLength) == "0.0" || floatTrueValue(operand1, eLength, sLength) == "-0.0")
			return "0" + operand2; // x == 0, return y
		else if (floatTrueValue(operand2, eLength, sLength) == "0.0"
				|| floatTrueValue(operand2, eLength, sLength) == "-0.0")
			return "0" + operand1; // y == 0, return x
		// 对阶
		if (e1 != e2) {

			// 判断谁的阶码比较大并对齐
			if (Integer.parseInt(integerTrueValue("0" + e1)) > Integer.parseInt(integerTrueValue("0" + e2))) {
				while (Integer.parseInt(integerTrueValue("0" + e1))
						- Integer.parseInt(integerTrueValue("0" + e2)) != 0) {
					sign2 = logRightShift(sign2, 1);
					// 判断两个数是否数量级差距太大以至于其中一个在对阶的过程中就被舍去
					if (Integer.parseInt(sign2) == 0) {
						return "0" + operand1;
					}
					// 小的阶码加一
					e2 = oneAdder(e2).substring(1);
				}
			} else {
				while (Integer.parseInt(integerTrueValue("0" + e2))
						- Integer.parseInt(integerTrueValue("0" + e1)) != 0) {
					sign1 = logRightShift(sign1, 1);
					if (Integer.parseInt(integerTrueValue(sign1)) == 0) {
						return "0" + operand2;
					}

					e1 = oneAdder(e1).substring(1);
				}
			}
		}
		String expo = e1;
		String significand = "";
		int length = ((2 + sLength + gLength) / 4 + 1) * 4;
		String sum = this.signedAddition(symbol1 + sign1, symbol2 + sign2, length);
		String result = sum.substring(length - gLength - sLength, length + 2);
		char symbol = sum.charAt(1);
		// 判断结果是否为0
		if (Integer.parseInt(integerTrueValue(result)) == 0) {
			return "0" + integerRepresentation("0", 1 + eLength + sLength);
		}
		// 规格化结果
		if (result.charAt(0) != '0') {

			expo = oneAdder(expo);

			result = logRightShift(result, 1);
			// 判断阶码是否溢出
			if (expo.charAt(0) == '1') {
				return "1" + expo.substring(1) + significand.substring(1, sLength + 1);
			}
			expo = expo.substring(1);
		}

		significand = result.substring(1);

		if (significand.charAt(0) == '0') {

			int exponent = Integer.parseInt(integerTrueValue(expo));
			while (significand.charAt(0) != '1') {
				significand = leftShift(significand, 1);
				exponent = exponent - 1;
				if (exponent == 0) {
					return "0" + integerRepresentation(Integer.toString(exponent), eLength)
							+ significand.substring(1, sLength + 1);
				}
				expo = integerRepresentation(Integer.toString(exponent), eLength);
			}
		}

		significand = significand.substring(1, sLength + 1);

		return "0" + symbol + expo + significand;

	}

	/**
	 * 浮点数减法，可调用{@link #floatAddition(String, String, int, int, int)
	 * floatAddition}方法实现。<br/>
	 * 例：floatSubtraction("00111111010100000", "00111111001000000", 8, 8, 8)
	 *
	 * @param operand1 二进制表示的被减数
	 * @param operand2 二进制表示的减数
	 * @param eLength  指数的长度，取值大于等于 4
	 * @param sLength  尾数的长度，取值大于等于 4
	 * @param gLength  保护位的长度
	 * @return 长度为2+eLength+sLength的字符串表示的相减结果，其中第1位指示是否指数上溢（溢出为1，否则为0），其余位从左到右依次为符号、指数（移码表示）、尾数（首位隐藏）。舍入策略为向0舍入
	 */
	public String floatSubtraction(String operand1, String operand2, int eLength, int sLength, int gLength) {
		// TODO YOUR CODE HERE.
		char symbol2 = (char) ('1' - operand2.charAt(0) + '0');
		operand2 = symbol2 + operand2.substring(1);
		String result = floatAddition(operand1, operand2, eLength, sLength, gLength);
		return result;

	}

	/**
	 * 浮点数乘法，可调用{@link #integerMultiplication(String, String, int)
	 * integerMultiplication}等方法实现。<br/>
	 * 例：floatMultiplication("00111110111000000", "00111111000000000", 8, 8)
	 *
	 * @param operand1 二进制表示的被乘数
	 * @param operand2 二进制表示的乘数
	 * @param eLength  指数的长度，取值大于等于 4
	 * @param sLength  尾数的长度，取值大于等于 4
	 * @return 长度为2+eLength+sLength的字符串表示的相乘结果,其中第1位指示是否指数上溢（溢出为1，否则为0），其余位从左到右依次为符号、指数（移码表示）、尾数（首位隐藏）。舍入策略为向0舍入
	 */
	public String floatMultiplication(String operand1, String operand2, int eLength, int sLength) {
		// TODO YOUR CODE HERE.
		if (Double.parseDouble(operand1.substring(1)) == 0.0 || Double.parseDouble(operand2.substring(1)) == 0.0) {
			String result = "";
			for (int i = 0; i < 2 + eLength + sLength; i++) {
				result = result + "0";
			}
			return result;
		}
		int expo1 = Integer.parseInt(integerTrueValue("0" + operand1.substring(1, 1 + eLength)));
		int expo2 = Integer.parseInt(integerTrueValue("0" + operand2.substring(1, 1 + eLength)));
		int bias = (int) (Math.pow(2, eLength - 1) - 1);

		char hide1 = '1';
		char hide2 = '1';
		if (expo1 == 0) {
			hide1 = '0';
		}
		if (expo2 == 0) {
			hide1 = '0';
		}

		String sign1 = hide1 + operand1.substring(eLength + 1);
		String sign2 = hide2 + operand2.substring(eLength + 1);
		// System.out.println(sign1);

		char symbol = '0';
		if (operand1.charAt(0) != operand2.charAt(0)) {
			symbol = '1';
		}

		if (floatTrueValue(operand1, eLength, sLength) == "0.0" || floatTrueValue(operand1, eLength, sLength) == "-0.0"
				|| floatTrueValue(operand2, eLength, sLength) == "0.0"
				|| floatTrueValue(operand2, eLength, sLength) == "-0.0") {
			String result = "";
			for (int i = 0; i < 1 + eLength + sLength; i++) {
				result += "0";
			}
			return symbol + result;
		}

		int exponent = expo1 + expo2 - bias;

		if (exponent > Math.pow(2, eLength) - 1) {
			String expo = "";
			String sign = "";
			for (int i = 0; i < eLength; i++) {
				expo += "1";
			}
			for (int i = 0; i < sLength; i++) {
				sign += "0";
			}

			return "1" + symbol + expo + sign;
		} else if (exponent < 0) {
			String symAndSign = "";
			for (int i = 0; i < eLength + sLength; i++) {
				symAndSign += "0";
			}

			return "0" + symbol + symAndSign;
		}

		String significand = sign2;
		int length = ((1 + sLength) / 4 + 1) * 4 - 1;

		for (int i = 0; i < 2 * length - sign2.length(); i++) {
			significand = "0" + significand;
		}

		for (int i = length - 1; i >= 0; i--) {
			if (significand.charAt(2 * length - 1) == '1') {
				significand = integerAddition("0" + significand.substring(0, length), "0" + sign1, length + 1)
						.substring(2) + significand.substring(length);
			}

			significand = logRightShift(significand, 1);
			// System.out.println(significand);
		}

		significand = significand.substring(length - sLength + 1, 2 * length);

		if (significand.charAt(0) == '1') {
			exponent++;

			if (exponent > Math.pow(2, eLength) - 1) {
				String expo = "";
				String sign = "";
				for (int i = 0; i < eLength; i++) {
					expo += "1";
				}
				for (int i = 0; i < sLength; i++) {
					sign += "0";
				}

				return "1" + symbol + expo + sign;
			}
		}
		significand = leftShift(significand, 1);

		while (significand.charAt(0) != '1') {
			exponent--;
			significand = leftShift(significand, 1);
			if (exponent == 0) {
				String expo = integerRepresentation("0", eLength);
				String sign = significand.substring(1, 1 + sLength);
				return "0" + symbol + expo + sign;
			}
		}
		// System.out.println(significand);

		String expo = integerRepresentation(Integer.toString(exponent), eLength + 1).substring(1);
		String sign = significand.substring(1, 1 + sLength);
		return "0" + symbol + expo + sign;

	}

	/**
	 * 浮点数除法，可调用{@link #integerDivision(String, String, int)
	 * integerDivision}等方法实现。<br/>
	 * 例：floatDivision("00111110111000000", "00111111000000000", 8, 8)
	 *
	 * @param operand1 二进制表示的被除数
	 * @param operand2 二进制表示的除数
	 * @param eLength  指数的长度，取值大于等于 4
	 * @param sLength  尾数的长度，取值大于等于 4
	 * @return 长度为2+eLength+sLength的字符串表示的相乘结果,其中第1位指示是否指数上溢（溢出为1，否则为0），其余位从左到右依次为符号、指数（移码表示）、尾数（首位隐藏）。舍入策略为向0舍入
	 */
	public String floatDivision(String operand1, String operand2, int eLength, int sLength) {
		// TODO YOUR CODE HERE.
		//被除数是0
		if (Double.parseDouble(operand1.substring(1)) == 0.0) {
			String result = "";
			for (int i = 0; i < 2 + eLength + sLength; i++) {
				result = result + "0";
			}
			return result;
		}
		//除数是0
		if (Double.parseDouble(operand2.substring(1)) == 0.0) {
			String result = "00";
			for (int i = 0; i < eLength; i++) {
				result = result + "1";

			}
			for (int i = 0; i < sLength; i++) {
				result = result + "0";

			}
			return result;

		}

		int expo1 = Integer.parseInt(integerTrueValue("0" + operand1.substring(1, 1 + eLength)));
		int expo2 = Integer.parseInt(integerTrueValue("0" + operand2.substring(1, 1 + eLength)));
		int bias = (int) (Math.pow(2, eLength - 1) - 1);

		char hide1 = '1';// “˛≤ÿŒª≥ı ºŒ™1
		char hide2 = '1';
		if (expo1 == 0) {
			hide1 = '0';
		}
		if (expo2 == 0) {
			hide1 = '0';
		}

		String sign1 = hide1 + operand1.substring(eLength + 1);
		String sign2 = hide2 + operand2.substring(eLength + 1);
		// System.out.println(sign1);

		char symbol = '0';
		if (operand1.charAt(0) != operand2.charAt(0)) {
			symbol = '1';
		}

		if (floatTrueValue(operand1, eLength, sLength) == "0.0"
				|| floatTrueValue(operand1, eLength, sLength) == "-0.0") {
			String result = "";
			for (int i = 0; i < 1 + eLength + sLength; i++) {
				result += "0";
			}
			return "0" + symbol + result;
		} else if (floatTrueValue(operand2, eLength, sLength) == "0.0"
				|| floatTrueValue(operand2, eLength, sLength) == "-0.0") {
			String expo = "";
			String sign = "";
			for (int i = 0; i < eLength; i++) {
				expo += "1";
			}
			for (int i = 0; i < sLength; i++) {
				sign += "0";
			}

			return "0" + symbol + expo + sign;
		}

		int exponent = expo1 - expo2 + bias;

		if (exponent > Math.pow(2, eLength) - 1) {
			String expo = "";
			String sign = "";
			for (int i = 0; i < eLength; i++) {
				expo += "1";
			}
			for (int i = 0; i < sLength; i++) {
				sign += "0";
			}

			return "1" + symbol + expo + sign;
		} else if (exponent < 0) {
			String symAndSign = "";
			for (int i = 0; i < eLength + sLength; i++) {
				symAndSign += "0";
			}

			return "0" + symbol + symAndSign;
		}

		String significand = sign1;
		int length = ((1 + sLength) / 4 + 1) * 4 - 1;

		for (int i = 0; i < length - sign1.length(); i++) {
			significand = "0" + significand;
		}

		for (int i = 0; i < length; i++) {
			significand = significand + "0";
		}

		// System.out.println(significand);

		for (int i = 0; i < length; i++) {
			// System.out.println(significand);
			String reminder = significand.substring(0, length);
			char c = '0';

			String temp = integerSubtraction("0" + reminder, "0" + sign2, length + 1).substring(2);
			if (Integer.parseInt(integerTrueValue(temp)) >= 0) {
				reminder = temp;
				c = '1';
			}
			significand = reminder + significand.substring(length, length * 2);
			significand = significand.substring(1) + c;

		}

		significand = significand.substring(length, 2 * length);
		// System.out.println(significand);

		while (significand.charAt(0) != '1') {
			exponent--;
			significand = leftShift(significand, 1);
			if (exponent == 0) {
				String expo = integerRepresentation("0", eLength);
				String sign = significand.substring(1, 1 + sLength);
				return "0" + symbol + expo + sign;
			}
		}

		String expo = integerRepresentation(Integer.toString(exponent), eLength + 1).substring(1);
		String sign = significand.substring(1, 1 + sLength);
		return "0" + symbol + expo + sign;

	}

	// 判断字符串是否为数字
	public static boolean isNumber(String value) {
		try {
			Double.parseDouble(value);

			return true;

		} catch (NumberFormatException e) {
			return false;
		}
	}

	public String NBCD(String num) {
		String ret = "";
		boolean isNeg = false;
		String isNega = "0";
		if (num.charAt(0) == '-') {
			isNega = "1";
			num = num.substring(1);
			isNeg = true;
		}
		int length = num.length();
		for (int i = 0; i < length; i++) {
			int integerNum = num.charAt(i) - 48;
			String temp = Integer.toBinaryString(integerNum);
			while (temp.length() < 4) {
				temp = "0" + temp;
			}
			ret += temp;
		}
		ret = isNega + ret;
		return ret;
	}

	public String BCDAddition(String opera1, String opera2) {
		/**
		 * 十进制加法，传入的参数默认是4的倍数，即调用NBCD方法之后，已经判断完前面的符号并且去掉了符号位
		 * 每四位调用一次cla方法，有一个进位就调用一个加六的方法作为第二次加法的第二个加数
		 * 最后还要判断一下是不是有了十的进位
		 */
		/*
		for(int i = opera1.length();i<4;i++){//防止两个长度小于4，但是应该传入的已经是规格化之后的
			opera1 = "0"+opera1;
		}
		for(int i = opera2.length();i<4;i++){
			opera2 = "0"+opera2;
		}
		*/
		if (opera1.length() < opera2.length()) {                                    //补齐，使两个数长度相等
			for (int i = opera1.length(); i < opera2.length(); i++) {
				opera1 = "0" + opera1;
			}
		} else if (opera1.length() > opera2.length()) {
			for (int i = opera2.length(); i < opera1.length(); i++) {
				opera2 = "0" + opera2;
			}
		}
		char c = '0';
		ArrayList<Character> cList = new ArrayList<>();
		String tenStr = "";                     //如果有大于十的，就用来加6
		String sixStr = "";                //有进位补6
		String oneStr = "";                //有进位还要在前面加个1
		String tempResult = "";
		String temp = "";
		String result = "";
		for (int i = opera1.length(); i > 0; i = i - 4) {
			String opa = opera1.substring(i - 4, i);
			String opb = opera2.substring(i - 4, i);
			temp = claAdder(opa, opb, c);
			cList.add(temp.charAt(0));                                                    //前一位的进位
			temp = temp.substring(1);                                                    //去掉进位的结果，后面要判断是否大于十
			if (temp.substring(0, 2).equals("11") | temp.substring(0, 3).equals("101")) {//判断是否大于10
				tenStr += "0110";
			}
			tempResult += temp;
		}
		for (int i = tempResult.length(); i > 0; i = i - 4) {
			result += tempResult.substring(i - 4, i);
		}            //得到的第一次运算完的结果

		for (int i = 0; i < cList.size(); i++) {
			if (cList.get(i) == '1') {
				sixStr += "0110";
				oneStr += "0001";
			}
		}
		oneStr += "0000";
		String ttempResult = "";
		String resultWithoutOne = "";
		String ret = result;
		if (!sixStr.equals("")) {                                                    //补6
			char cc = '0';
			String ttemp = "";
			for (int i = sixStr.length(); i < ret.length(); i++) {                        //补齐长度
				sixStr = "0" + sixStr;
			}
			ArrayList<Character> ccList = new ArrayList<>();
			for (int i = ret.length(); i > 0; i = i - 4) {
				String opa = ret.substring(i - 4, i);
				String opb = sixStr.substring(i - 4, i);
				ttemp = claAdder(opa, opb, cc);
				cc = ttemp.charAt(0);
				ccList.add(cc);
				ttemp = ttemp.substring(1);
				ttempResult += ttemp;
			}
			for (int i = ret.length(); i > 0; i = i - 4) {
				resultWithoutOne += ttempResult.substring(i - 4, i);
			}

			//下面开始跟1相加  啊啊啊啊啊啊啊啊
			char ccc = '0';
			String tttemp = "";
			String tttempResult = "";
			for (int i = resultWithoutOne.length(); i < oneStr.length(); i++) {                        //补齐长度
				resultWithoutOne = "0" + resultWithoutOne;
			}
			ArrayList<Character> cccList = new ArrayList<>();
			for (int i = oneStr.length(); i > 0; i = i - 4) {
				String opa = oneStr.substring(i - 4, i);
				String opb = resultWithoutOne.substring(i - 4, i);
				tttemp = claAdder(opa, opb, ccc);
				//
				ccc = tttemp.charAt(0);
				cccList.add(ccc);
				tttemp = tttemp.substring(1);
				tttempResult += tttemp;
			}
			ret = "";
			//System.out.println(ttempResult);
			for (int i = tttempResult.length(); i > 0; i = i - 4) {
				ret += tttempResult.substring(i - 4, i);
			}
		}
		//下面开始补大于10的补6   不写了 一个意思 加个10就行了
		return ret;
	}
	public static void main(String[] args) {
		ALU A = new ALU();
		System.out.println(A.BCDAddition("00100101","00111001"));
	}

}