package com.hcicloud.sap.common.encrypt;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Security
{
    private static char[] hex = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

    public static String hashSha256(String content)
    {
        String strDes = null;
        try
        {
            content = URLEncoder.encode(content, "UTF-8");
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashData = md.digest(content.getBytes("UTF-8"));
            strDes = hexEncode(hashData);
        }
        catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {}catch (UnsupportedEncodingException localUnsupportedEncodingException) {}
        return strDes;
    }

    public static String encryptByCbc(String content)
    {
        String encryptByCbc = null;
        try
        {
            encryptByCbc = encryptByCbc(createKey(), content);
        }
        catch (InvalidKeyException localInvalidKeyException) {}catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {}catch (NoSuchPaddingException localNoSuchPaddingException) {}catch (InvalidAlgorithmParameterException localInvalidAlgorithmParameterException) {}catch (UnsupportedEncodingException localUnsupportedEncodingException) {}catch (IllegalBlockSizeException localIllegalBlockSizeException) {}catch (BadPaddingException localBadPaddingException) {}
        return encryptByCbc;
    }

    public static String encryptByCbc(byte[] encryptKey, String content)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException
    {
        SecretKeySpec key = new SecretKeySpec(encryptKey, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(1, key, new IvParameterSpec(createIv()));
        byte[] data = content.getBytes("UTF-8");
        byte[] encryptData = cipher.doFinal(data);

        return hexEncode(encryptData);
    }

    public static String decryptByCbc(String content)
    {
        String decryptByCbc = null;
        try
        {
            decryptByCbc = decryptByCbc(createKey(), content, "UTF-8");
        }
        catch (InvalidKeyException localInvalidKeyException) {}catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {}catch (NoSuchPaddingException localNoSuchPaddingException) {}catch (InvalidAlgorithmParameterException localInvalidAlgorithmParameterException) {}catch (IllegalBlockSizeException localIllegalBlockSizeException) {}catch (BadPaddingException localBadPaddingException) {}catch (UnsupportedEncodingException localUnsupportedEncodingException) {}
        return decryptByCbc;
    }

    public static String decryptByCbc(byte[] encryptKey, String content, String charsetName)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException
    {
        byte[] decryptData = decryptByCbc(encryptKey, content);

        return new String(decryptData, charsetName);
    }

    private static byte[] decryptByCbc(byte[] encryptKey, String content)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException
    {
        SecretKeySpec key = new SecretKeySpec(encryptKey, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(2, key, new IvParameterSpec(createIv()));
        byte[] data = hexDecode(content);
        byte[] decryptData = cipher.doFinal(data);
        return decryptData;
    }

    private static String hexEncode(byte[] bytes)
    {
        char[] hexChar = new char[bytes.length * 2];
        for (int i = 0; i < bytes.length; i++)
        {
            hexChar[(i * 2)] = hex[(bytes[i] >>> 4 & 0xF)];
            hexChar[(i * 2 + 1)] = hex[(bytes[i] & 0xF)];
        }
        return new String(hexChar);
    }

    private static byte[] hexDecode(String data)
    {
        char[] hexChar = data.toUpperCase(Locale.ENGLISH).toCharArray();

        byte[] bytes = new byte[hexChar.length / 2];
        for (int i = 0; i < bytes.length; i++)
        {
            byte high = charToByte(hexChar[(i * 2)]);
            byte low = charToByte(hexChar[(i * 2 + 1)]);
            bytes[i] = ((byte)(high << 4 | low));
        }
        return bytes;
    }

    private static byte charToByte(char c)
    {
        return (byte)(c < 'A' ? c - '0' : c - 'A' + 10);
    }

    private static byte[] createKey()
    {
        char[] key = getKey512();

        byte[] aesKey = new byte[16];
        int i = 0;
        for (int k = 1; i < aesKey.length; i++)
        {
            k += i * 3;
            aesKey[i] = ((byte)key[k]);
        }
        return aesKey;
    }

    private static byte[] createIv()
    {
        char[] key = getKey512();

        byte[] cbcIv = new byte[16];
        for (int i = 0; i < cbcIv.length; i++) {
            cbcIv[i] = ((byte)key[i]);
        }
        return cbcIv;
    }

    private static char[] getKey512()
    {
        char[] key = new char[512];
        key[0] = '';
        key[1] = 'é';
        key[2] = '\033';
        key[3] = '*';
        key[4] = '*';
        key[5] = '¬';
        key[6] = 'Ý';
        key[7] = '';
        key[8] = 'Ò';
        key[9] = 'Ñ';
        key[10] = '«';
        key[11] = 'p';
        key[12] = 'ô';
        key[13] = '';
        key[14] = 'S';
        key[15] = 'ô';
        key[16] = 'J';
        key[17] = '¦';
        key[18] = 'é';
        key[19] = 'B';
        key[20] = '(';
        key[21] = '¦';
        key[22] = 'å';
        key[23] = 'É';
        key[24] = '>';
        key[25] = 'Á';
        key[26] = '';
        key[27] = '';
        key[28] = 'Ý';
        key[29] = 'T';
        key[30] = 't';
        key[31] = '\013';
        key[32] = 'b';
        key[33] = '';
        key[34] = 'é';
        key[35] = '\033';
        key[36] = '*';
        key[37] = '¬';
        key[38] = 'Ý';
        key[39] = '';
        key[40] = 'Ò';
        key[41] = 'Ñ';
        key[42] = '«';
        key[43] = 'p';
        key[44] = 'ô';
        key[45] = '';
        key[46] = 'S';
        key[47] = 'ô';
        key[48] = 'J';
        key[49] = '¦';
        key[50] = 'é';
        key[51] = 'B';
        key[52] = '(';
        key[53] = '¦';
        key[54] = 'å';
        key[55] = 'É';
        key[56] = '>';
        key[57] = 'Á';
        key[58] = '';
        key[59] = '';
        key[60] = 'Ý';
        key[61] = 'T';
        key[62] = 't';
        key[63] = '\013';
        key[64] = 'b';
        key[65] = '';
        key[66] = 'é';
        key[67] = '\033';
        key[68] = '*';
        key[69] = '¬';
        key[70] = 'Ý';
        key[71] = '';
        key[72] = 'Ò';
        key[73] = 'Ñ';
        key[74] = '«';
        key[75] = 'p';
        key[76] = 'ô';
        key[77] = '';
        key[78] = 'S';
        key[79] = 'ô';
        key[80] = 'J';
        key[81] = '¦';
        key[82] = 'é';
        key[83] = 'B';
        key[84] = '(';
        key[85] = '¦';
        key[86] = 'å';
        key[87] = 'É';
        key[88] = '>';
        key[89] = 'Á';
        key[90] = '';
        key[91] = '';
        key[92] = 'Ý';
        key[93] = 'T';
        key[94] = 't';
        key[95] = '\013';
        key[96] = 'b';
        key[97] = '';
        key[98] = 'é';
        key[99] = '\033';
        key[100] = '*';
        key[101] = '¬';
        key[102] = 'Ý';
        key[103] = '';
        key[104] = 'Ò';
        key[105] = 'Ñ';
        key[106] = '«';
        key[107] = 'p';
        key[108] = 'ô';
        key[109] = '';
        key[110] = 'S';
        key[111] = 'ô';
        key[112] = 'J';
        key[113] = '¦';
        key[114] = 'é';
        key[115] = 'B';
        key[116] = '(';
        key[117] = '¦';
        key[118] = 'å';
        key[119] = 'É';
        key[120] = '>';
        key[121] = 'Á';
        key[122] = '';
        key[123] = '';
        key[124] = 'Ý';
        key[125] = 'T';
        key[126] = 't';
        key[127] = '\013';
        key[''] = 'b';
        key[''] = '';
        key[''] = 'é';
        key[''] = '\033';
        key[''] = '*';
        key[''] = '¬';
        key[''] = 'Ý';
        key[''] = '';
        key[''] = 'Ò';
        key[''] = 'Ñ';
        key[''] = '«';
        key[''] = 'p';
        key[''] = 'ô';
        key[''] = '';
        key[''] = 'S';
        key[''] = 'ô';
        key[''] = 'J';
        key[''] = '¦';
        key[''] = 'é';
        key[''] = 'B';
        key[''] = '(';
        key[''] = '¦';
        key[''] = 'å';
        key[''] = 'É';
        key[''] = '>';
        key[''] = 'Á';
        key[''] = '';
        key[''] = '';
        key[''] = 'Ý';
        key[''] = 'T';
        key[''] = 't';
        key[''] = '\013';
        key[' '] = 'b';
        key['¡'] = '';
        key['¢'] = 'é';
        key['£'] = '\033';
        key['¤'] = '*';
        key['¥'] = '¬';
        key['¦'] = 'Ý';
        key['§'] = '';
        key['¨'] = 'Ò';
        key['©'] = 'Ñ';
        key['ª'] = '«';
        key['«'] = 'p';
        key['¬'] = 'ô';
        key['­'] = '';
        key['®'] = 'S';
        key['¯'] = 'ô';
        key['°'] = 'J';
        key['±'] = '¦';
        key['²'] = 'é';
        key['³'] = 'B';
        key['´'] = '(';
        key['µ'] = '¦';
        key['¶'] = 'å';
        key['·'] = 'É';
        key['¸'] = '>';
        key['¹'] = 'Á';
        key['º'] = '';
        key['»'] = '';
        key['¼'] = 'Ý';
        key['½'] = 'T';
        key['¾'] = 't';
        key['¿'] = '\013';
        key['À'] = 'b';
        key['Á'] = '';
        key['Â'] = 'é';
        key['Ã'] = '\033';
        key['Ä'] = '*';
        key['Å'] = '¬';
        key['Æ'] = 'Ý';
        key['Ç'] = '';
        key['È'] = 'Ò';
        key['É'] = 'Ñ';
        key['Ê'] = '«';
        key['Ë'] = 'p';
        key['Ì'] = 'ô';
        key['Í'] = '';
        key['Î'] = 'S';
        key['Ï'] = 'ô';
        key['Ð'] = 'J';
        key['Ñ'] = '¦';
        key['Ò'] = 'é';
        key['Ó'] = 'B';
        key['Ô'] = '(';
        key['Õ'] = '¦';
        key['Ö'] = 'å';
        key['×'] = 'É';
        key['Ø'] = '>';
        key['Ù'] = 'Á';
        key['Ú'] = '';
        key['Û'] = '';
        key['Ü'] = 'Ý';
        key['Ý'] = 'T';
        key['Þ'] = 't';
        key['ß'] = '\013';
        key['à'] = 'b';
        key['á'] = '';
        key['â'] = 'é';
        key['ã'] = '\033';
        key['ä'] = '*';
        key['å'] = '¬';
        key['æ'] = 'Ý';
        key['ç'] = '';
        key['è'] = 'Ò';
        key['é'] = 'Ñ';
        key['ê'] = '«';
        key['ë'] = 'p';
        key['ì'] = 'ô';
        key['í'] = '';
        key['î'] = 'S';
        key['ï'] = 'ô';
        key['ð'] = 'J';
        key['ñ'] = '¦';
        key['ò'] = 'é';
        key['ó'] = 'B';
        key['ô'] = '(';
        key['õ'] = '¦';
        key['ö'] = 'å';
        key['÷'] = 'É';
        key['ø'] = '>';
        key['ù'] = 'Á';
        key['ú'] = '';
        key['û'] = '';
        key['ü'] = 'Ý';
        key['ý'] = 'T';
        key['þ'] = 't';
        key['ÿ'] = '\013';
        key[256] = 'b';
        key[257] = '';
        key[258] = 'é';
        key[259] = '\033';
        key[260] = '*';
        key[261] = '¬';
        key[262] = 'Ý';
        key[263] = '';
        key[264] = 'Ò';
        key[265] = 'Ñ';
        key[266] = '«';
        key[267] = 'p';
        key[268] = 'ô';
        key[269] = '';
        key[270] = 'S';
        key[271] = 'ô';
        key[272] = 'J';
        key[273] = '¦';
        key[274] = 'é';
        key[275] = 'B';
        key[276] = '(';
        key[277] = '¦';
        key[278] = 'å';
        key[279] = 'É';
        key[280] = '>';
        key[281] = 'Á';
        key[282] = '';
        key[283] = '';
        key[284] = 'Ý';
        key[285] = 'T';
        key[286] = 't';
        key[287] = '\013';
        key[288] = 'b';
        key[289] = '';
        key[290] = 'é';
        key[291] = '\033';
        key[292] = '*';
        key[293] = '¬';
        key[294] = 'Ý';
        key[295] = '';
        key[296] = 'Ò';
        key[297] = 'Ñ';
        key[298] = '«';
        key[299] = 'p';
        key[300] = 'ô';
        key[301] = '';
        key[302] = 'S';
        key[303] = 'ô';
        key[304] = 'J';
        key[305] = '¦';
        key[306] = 'é';
        key[307] = 'B';
        key[308] = '(';
        key[309] = '¦';
        key[310] = 'å';
        key[311] = 'É';
        key[312] = '>';
        key[313] = 'Á';
        key[314] = '';
        key[315] = '';
        key[316] = 'Ý';
        key[317] = 'T';
        key[318] = 't';
        key[319] = '\013';
        key[320] = 'b';
        key[321] = '';
        key[322] = 'é';
        key[323] = '\033';
        key[324] = '*';
        key[325] = '¬';
        key[326] = 'Ý';
        key[327] = '';
        key[328] = 'Ò';
        key[329] = 'Ñ';
        key[330] = '«';
        key[331] = 'p';
        key[332] = 'ô';
        key[333] = '';
        key[334] = 'S';
        key[335] = 'ô';
        key[336] = 'J';
        key[337] = '¦';
        key[338] = 'é';
        key[339] = 'B';
        key[340] = '(';
        key[341] = '¦';
        key[342] = 'å';
        key[343] = 'É';
        key[344] = '>';
        key[345] = 'Á';
        key[346] = '';
        key[347] = '';
        key[348] = 'Ý';
        key[349] = 'T';
        key[350] = 't';
        key[351] = '\013';
        key[352] = 'b';
        key[353] = '';
        key[354] = 'é';
        key[355] = '\033';
        key[356] = '*';
        key[357] = '¬';
        key[358] = 'Ý';
        key[359] = '';
        key[360] = 'Ò';
        key[361] = 'Ñ';
        key[362] = '«';
        key[363] = 'p';
        key[364] = 'ô';
        key[365] = '';
        key[366] = 'S';
        key[367] = 'ô';
        key[368] = 'J';
        key[369] = '¦';
        key[370] = 'é';
        key[371] = 'B';
        key[372] = '(';
        key[373] = '¦';
        key[374] = 'å';
        key[375] = 'É';
        key[376] = '>';
        key[377] = 'Á';
        key[378] = '';
        key[379] = '';
        key[380] = 'Ý';
        key[381] = 'T';
        key[382] = 't';
        key[383] = '\013';
        key[384] = 'b';
        key[385] = '';
        key[386] = 'é';
        key[387] = '\033';
        key[388] = '*';
        key[389] = '¬';
        key[390] = 'Ý';
        key[391] = '';
        key[392] = 'Ò';
        key[393] = 'Ñ';
        key[394] = '«';
        key[395] = 'p';
        key[396] = 'ô';
        key[397] = '';
        key[398] = 'S';
        key[399] = 'ô';
        key[400] = 'J';
        key[401] = '¦';
        key[402] = 'é';
        key[403] = 'B';
        key[404] = '(';
        key[405] = '¦';
        key[406] = 'å';
        key[407] = 'É';
        key[408] = '>';
        key[409] = 'Á';
        key[410] = '';
        key[411] = '';
        key[412] = 'Ý';
        key[413] = 'T';
        key[414] = 't';
        key[415] = '\013';
        key[416] = 'b';
        key[417] = '';
        key[418] = 'é';
        key[419] = '\033';
        key[420] = '*';
        key[421] = '¬';
        key[422] = 'Ý';
        key[423] = '';
        key[424] = 'Ò';
        key[425] = 'Ñ';
        key[426] = '«';
        key[427] = 'p';
        key[428] = 'ô';
        key[429] = '';
        key[430] = 'S';
        key[431] = 'ô';
        key[432] = 'J';
        key[433] = '¦';
        key[434] = 'é';
        key[435] = 'B';
        key[436] = '(';
        key[437] = '¦';
        key[438] = 'å';
        key[439] = 'É';
        key[440] = '>';
        key[441] = 'Á';
        key[442] = '';
        key[443] = '';
        key[444] = 'Ý';
        key[445] = 'T';
        key[446] = 't';
        key[447] = '\013';
        key[448] = 'b';
        key[449] = '';
        key[450] = 'é';
        key[451] = '\033';
        key[452] = '*';
        key[453] = '¬';
        key[454] = 'Ý';
        key[455] = '';
        key[456] = 'Ò';
        key[457] = 'Ñ';
        key[458] = '«';
        key[459] = 'p';
        key[460] = 'ô';
        key[461] = '';
        key[462] = 'S';
        key[463] = 'ô';
        key[464] = 'J';
        key[465] = '¦';
        key[466] = 'é';
        key[467] = 'B';
        key[468] = '(';
        key[469] = '¦';
        key[470] = 'å';
        key[471] = 'É';
        key[472] = '>';
        key[473] = 'Á';
        key[474] = '';
        key[475] = '';
        key[476] = 'Ý';
        key[477] = 'T';
        key[478] = 't';
        key[479] = '\013';
        key[480] = 'b';
        key[481] = '';
        key[482] = 'é';
        key[483] = '\033';
        key[484] = '*';
        key[485] = '¬';
        key[486] = 'Ý';
        key[487] = '';
        key[488] = 'Ò';
        key[489] = 'Ñ';
        key[490] = '«';
        key[491] = 'p';
        key[492] = 'ô';
        key[493] = '';
        key[494] = 'S';
        key[495] = 'ô';
        key[496] = 'J';
        key[497] = '¦';
        key[498] = 'é';
        key[499] = 'B';
        key[500] = '(';
        key[501] = '¦';
        key[502] = 'å';
        key[503] = 'É';
        key[504] = '>';
        key[505] = 'Á';
        key[506] = '';
        key[507] = '';
        key[508] = 'Ý';
        key[509] = 'T';
        key[510] = 't';
        key[511] = '\013';
        return key;
    }
}

