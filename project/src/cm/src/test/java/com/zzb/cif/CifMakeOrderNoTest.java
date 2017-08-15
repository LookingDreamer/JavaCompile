package com.zzb.cif;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

import com.cninsure.cssj.JUnit4ClassRunner;
import com.cninsure.jobpool.PoolService;
import com.cninsure.system.manager.scm.INSCSyncService;
import com.cninsure.system.manager.scr.INSCFdcomManager;
import com.common.GenerateSequenceUtil;
import com.common.HttpClientUtil;
import com.zzb.app.service.AppQuotationService;
import com.zzb.cm.Interface.service.InterFaceService;
import com.zzb.cm.Interface.service.impl.InterFaceServiceImpl;
import com.zzb.cm.controller.CifController;
import com.zzb.cm.controller.vo.FindLastClaimBackInfoVo;
import com.zzb.cm.dao.INSBOrderDao;
import com.zzb.cm.dao.INSBQuotetotalinfoDao;
import com.zzb.cm.entity.INSBOrder;
import com.zzb.cm.entity.INSBQuotetotalinfo;
import com.zzb.cm.service.INSBOrdernumberService;
import com.zzb.conf.controller.INSBWorkFlowDataContorller;
import com.zzb.conf.dao.INSBPaychannelmanagerDao;
import com.zzb.conf.dao.INSBWorkflowmainDao;
import com.zzb.conf.dao.INSBWorkflowsubDao;
import com.zzb.conf.dao.INSBWorkflowsubtrackDao;
import com.zzb.conf.entity.INSBWorkflowmain;
import com.zzb.conf.entity.INSBWorkflowsub;
import com.zzb.conf.service.INSBAgentService;
import com.zzb.conf.service.INSBAutoconfigService;
import com.zzb.conf.service.INSBAutoconfigshowService;
import com.zzb.conf.service.INSBWorkTimeService;
import com.zzb.conf.service.INSBWorkflowmainService;
import com.zzb.ldap.LdapMd5;
import com.zzb.mobile.service.AppOtherRequestService;
import com.zzb.mobile.service.INSBPayChannelService;
import com.zzb.model.WorkFlow4TaskModel;

@RunWith(JUnit4ClassRunner.class)
@WebAppConfiguration(value = "src/main/webapp")
@ContextConfiguration(locations = { "classpath:config/spring-config.xml",
		"classpath:config/spring-security-config.xml",
		"classpath:config/spring-mvc-config.xml", })
public class CifMakeOrderNoTest {
	@Resource
	private INSBOrdernumberService insbOrdernumberService;
	@Resource
	private AppQuotationService appQuotationService;
	@Resource
	private INSBWorkflowsubtrackDao dao;
	@Resource
	private InterFaceService interf;
	@Resource
	private INSBAgentService agtService;
	@Resource
	private INSCSyncService syncs;
	@Resource
	private INSCFdcomManager inscFdcomManager;

	@Resource
	private INSBWorkFlowDataContorller dataC;
	@Resource
	private INSBWorkflowmainService workflowmainService;

	@Resource
	private INSBQuotetotalinfoDao insbQuotetotalinfoDao;
	@Resource
	private INSBOrderDao insbOrderDao;
	@Resource
	private INSBWorkflowmainDao maindao;
	@Resource
	private INSBWorkflowsubDao subdao;
	@Resource
	private INSBWorkTimeService insbwt;
	@Resource
	private INSBAutoconfigService insbAutoconfigService;
	@Resource
	private INSBAutoconfigshowService insbAutoconfigshowService;
	@Resource
	private InterFaceService interF;
	@Resource
	private CifController cifc;
	@Resource
	private InterFaceServiceImpl fs;
	@Resource
	private INSBWorkflowmainService ms;
	@Resource
	private AppOtherRequestService appOtherRequestService;
	@Resource
	INSBWorkFlowDataContorller c;
	@Resource
	PoolService ps;
	
	@Resource
	INSBPaychannelmanagerDao pd;
	
	@Test
	public void testGetPayId(){
		System.out.println(pd.selectPaychannelIdByAgreementId("1244191435", "201644"));
	}
	
	

//	@Test
	public void testDis() {
		com.cninsure.jobpool.Task task = new com.cninsure.jobpool.Task();
		task.setPrvcode("20263704");
		task.setProInstanceId("426390");
		task.setSonProInstanceId("426439");
		task.setTaskcode("18");
		task.setTaskName("人工核保");
		task.setDispatchflag(false);
		ps.dispathUserByTask(task);
	}

	// @Test
	public void testQuote() {
		// WorkFlow4TaskModel [mainInstanceId=422004, subInstanceId=null,
		// providerId=, taskName=信息录入, taskCode=1, taskStatus=Reserved,
		// dataFlag=2]

		WorkFlow4TaskModel m = new WorkFlow4TaskModel();
		m.setMainInstanceId("54303");
		m.setSubInstanceId(null);
		m.setProviderId("");
		m.setTaskName("信息录入");
		m.setTaskCode("1");
		m.setTaskStatus("Reserved");
		m.setDataFlag(2);
		c.startTaskFromWorlFlow(m);

		// interF.getPacket("321187", "20163703", "robot");
		// FindLastClaimBackInfoVo claimvo = new FindLastClaimBackInfoVo();
		// claimvo.setTaskId("325446");
		// claimvo.setJobnum("620535168");
		// cifc.findLastClaimBackInfo(claimvo);'
		// ms.getContracthbType("170772", "208844", "3", "underwriting");
		// int i = appOtherRequestService
		// .selectWorkFlowRuleInfo("22991", "208844");
		// System.out.println(i);
		// appOtherRequestService.updateClosedWorkFlowRuleInfo("22991",
		// "208844");
		// i = appOtherRequestService.selectWorkFlowRuleInfo("22991", "208844");
		// System.out.println(i);
	}

	public static void main(String[] args) {
		// String orderno =GenerateSequenceUtil.generateSequenceNo("201551",
		// "201558");
		// System.out.println(orderno);
		// System.out.println(BigDecimal.valueOf(Double.valueOf("9.105")).setScale(2,
		// RoundingMode.HALF_UP)); ;
		Map<String, String> params = new HashMap<String, String>();
		// 规则
		// params.put("datas",
		// "{\"processinstanceid\":\"327388\",\"data\":{\"result\":\"3\"},\"taskName\":\"规则报价\",\"userid\":\"admin\"}");
		// 精灵核保
		// params.put("datas",
		// "{\"processinstanceid\":326920,\"data\":{\"result\":\"1\",\"skipSpirit\":\"1\"},\"taskName\":\"精灵核保\",\"userid\":\"admin\"}");
		// 人工核保
		// {\"processinstanceid\":65013,\"data\":{\"result\":\"2\"},\"taskName\":\"人工核保\",\"userid\":\"chengan\"}
		// params.put("datas",
		// "{\"processinstanceid\":326666,\"data\":{\"result\":\"2\"},\"taskName\":\"人工核保\",\"userid\":\"admin\"}");
		// String result =
		// HttpClientUtil.doGet("http://119.29.37.183:8080/workflow//process/completeSubTask",
		// params);

		// params.put("datas","{\"processinstanceid\":385151,\"data\":{\"result\":\"1\"},\"taskName\":\"精灵承保\",\"userid\":\"admin\"}");
		// String result =
		// HttpClientUtil.doGet("http://119.29.37.183:8080/workflow//process/completeTask",
		// params);
		// int[] a = { 419511, 418970, 418731, 418003, 417332, 417277, 416722,
		// 416024, 415735, 414680, 414638, 414224, 413741, 411912,
		// 410841, 410896, 410354, 408763, 408245, 408006, 408010, 407478,
		// 406319, 405252, 403700, 402538, 402387, 402374, 401279, 400816,
		// 400254, 400183, 400209, 399912, 396754, 394447, 393798, 393562,
		// 392907, 391057, 391096, 388633, 385400, 385310, 383285, 383136,
		// 382638, 380667, 380304, 379520, 377936, 376239, 375221, 372948,
		// 371884, 371669, 369523, 368755, 367476, 367390, 367257, 367096,
		// 366203, 366211, 365600, 365234, 364426, 364104, 363567, 363340,
		// 363413, 362856, 361291, 361182, 361038, 359060, 358435, 357905,
		// 357836, 357668, 357461, 357381, 357385, 357272, 357136, 356584,
		// 356521, 356096, 355494, 355024, 354070, 353539, 353382, 350156,
		// 348790, 348624, 348473, 347297, 346223, 344011, 343253, 342182,
		// 341532, 340598, 340034, 338529, 338490, 337456, 336439, 336249,
		// 336455, 334832, 334816, 334529, 327282, 326413, 326231, 324661,
		// 320305, 320197, 320233, 319953, 317984, 316607, 315204, 314413,
		// 313611, 313390, 312912, 309719, 308948, 306909, 305787, 305047,
		// 303873, 303201, 302814, 299856, 297226, 295060, 293235, 290714,
		// 290167, 290203, 289948, 289500, 288990, 288393, 288125, 284288,
		// 282450, 282274, 279327, 278629, 275904, 275077, 273833, 273242,
		// 272891, 272829, 272952, 272395, 272287, 271131, 270467, 270221,
		// 268916, 266687, 266622, 265579, 264356, 263569, 261316, 261231,
		// 260306, 258187, 257034, 256278, 255790, 255674, 254889, 254522,
		// 254319, 254102, 252746, 252358, 244665, 243013, 242158, 241986,
		// 241721, 238119, 233174, 231958, 230530, 228804, 225845, 225062,
		// 223072, 219252, 218801, 218218, 217938, 217751, 217367, 216939,
		// 216571, 213789, 213146, 212786, 212650, 212285, 209746, 209429,
		// 208409, 208290, 208133, 208039, 207645, 205656, 205462, 205418,
		// 204855, 203903, 203720, 203037, 202914, 202168, 202118, 200887,
		// 198404, 196173, 195647, 195314, 191766, 191380, 191253, 189177,
		// 187644, 186340, 186092, 185403, 183633, 182619, 181993, 181371,
		// 180913, 177690, 176266, 175603, 175462, 174306, 172563, 172295,
		// 171982, 171263, 168920, 168595, 168500, 167889, 166956, 165046,
		// 163796, 161170, 159636, 159625, 158318, 158140, 157711, 157298,
		// 157215, 156820, 156126, 155656, 154029, 150143, 149026, 147185,
		// 146233, 144861, 142013, 141803, 141256, 139837, 139647, 138203,
		// 137735, 137239, 135761, 135508, 132883, 132706, 132572, 129687,
		// 128868, 126722, 126356, 125502, 125285, 124231, 121047, 118240,
		// 116777, 116684, 115741, 114172, 111834, 106608, 101525, 97144,
		// 91201, 90930, 88791, 87412, 87075, 79278, 77963, 54789, 53330,
		// 53212, 27756, 26856, 26781, 26498, 26493, 26437, 26150, 24898,
		// 24268, 24254, 23688, 23617, 23489, 23459, 23394, 23341, 23187,
		// 21370, 19949, 19531, 19524, 19415, 18987, 18808, 18523, 18278,
		// 18226, 18140, 18135, 17945, 17910, 17707, 17416 };
		// int[] a = { 419184, 417907, 415986, 413712, 413006, 412861, 410204,
		// 397479, 396684, 391001, 383416, 382640, 381489, 381026, 376110,
		// 375335, 366507, 358319, 341385, 325011, 320497, 318994, 311863,
		// 302795, 291474, 284659, 284453, 283967, 282109, 280807, 280557,
		// 275543, 274602, 262966, 243554, 235606, 221612, 209240, 201668,
		// 166166, 160755, 157443, 144241, 144224, 144211, 128052, 127613,
		// 127594, 127510, 125809, 121275, 114613, 112562, 111224, 111205,
		// 107275, 89259, 88639, 85573, 82653, 80914, 76531, 72777, 70402,
		// 69787, 68270, 67956, 67931, 67568, 38714, 37492 };

//		int[] a = { 420182, 420148, 419984, 419895, 419865, 419812, 419792,
//				419749, 419722, 419584, 419570, 419554, 419434, 419181, 419122,
//				419055, 418902, 418762, 418649, 418662, 418634, 418575, 418426,
//				418028, 417834, 417679, 417326, 416982, 417037, 416694, 416683,
//				416667, 416604, 416113, 415424, 415205, 415150, 414803, 414736,
//				414774, 414724, 414420, 414042, 413644, 413519, 413329, 413194,
//				412800, 412727, 412654, 412590, 412360, 412243, 412008, 411545,
//				411453, 411172, 411145, 410432, 410433, 410497, 410003, 409813,
//				409790, 409478, 409269, 409511, 409159, 409168, 409210, 408888,
//				408853, 408493, 408205, 408029, 407850, 407470, 407424, 407176,
//				406999, 406929, 406815, 406727, 406721, 406606, 405831, 405289,
//				404984, 404903, 404028, 404027, 403844, 403002, 402675, 402499,
//				402253, 402093, 401877, 401835, 401586, 401338, 401263, 401230,
//				400912, 400493, 400234, 400068, 400097, 399980, 399881, 399731,
//				399551, 399570, 399051, 398901, 398768, 398489, 398407, 397708,
//				397277, 397236, 397166, 396518, 396103, 395785, 395739, 395678,
//				395558, 395482, 395129, 395034, 394843, 394745, 394481, 394184,
//				393884, 393789, 393532, 393462, 393408, 393505, 393538, 393281,
//				393221, 393168, 392989, 392974, 393018, 392998, 392919, 392706,
//				392419, 392204, 391851, 391925, 391500, 391401, 391149, 391119,
//				390851, 390738, 390742, 390408, 390359, 390243, 390077, 389456,
//				389068, 388919, 388956, 388943, 388570, 388530, 388308, 388276,
//				389650, 388035, 387554, 387408, 387041, 386206, 385651, 385069,
//				384502, 384122, 384037, 383324, 382587, 382387, 382373, 382218,
//				382170, 382211, 382056, 381933, 381985, 381862, 381530, 381499,
//				381524, 381470, 381449, 381006, 380895, 380637, 380221, 380125,
//				380094, 379456, 379370, 379398, 379289, 379627, 379219, 379125,
//				378803, 378619, 378601, 378521, 378056, 378073, 377468, 377340,
//				377309, 377312, 377216, 377130, 376969, 376989, 376670, 376595,
//				376475, 376274, 376162, 376235, 376168, 376222, 375957, 375921,
//				375771, 375729, 376253, 375975, 375462, 375411, 375283, 375090,
//				375051, 374957, 374767, 374590, 374540, 374405, 374245, 374067,
//				373605, 373706, 373432, 373370, 373183, 372745, 372592, 372192,
//				372115, 372069, 371765, 371441, 371301, 371208, 371196, 371093,
//				370568, 370514, 370402, 370223, 369389, 368716, 367851, 367774,
//				367678, 367474, 366867, 366839, 366831, 366766, 366027, 365930,
//				365859, 365481, 365442, 365384, 365157, 364774, 364711, 364766,
//				364627, 364557, 364149, 363720, 363650, 363140, 362816, 361553,
//				361481, 361399, 361313, 361252, 361228, 361170, 361073, 360690,
//				360704, 360469, 360232, 360141, 360014, 359433, 359398, 359287,
//				359217, 359187, 359008, 358983, 358707, 358671, 358563, 358550,
//				358555, 358522, 358485, 358132, 358097, 358118, 358000, 357918,
//				357856, 357832, 357355, 356935, 356853, 356656, 356536, 356474,
//				356345, 356194, 355959, 355702, 355853, 355013, 354889, 354443,
//				354361, 354135, 353934, 353754, 353585, 353443, 353080, 352740,
//				352215, 352146, 351413, 351206, 350702, 350480, 350398, 350326,
//				350202, 350143, 349704, 349319, 349259, 349119, 348924, 348881,
//				348542, 348137, 348074, 347862, 347889, 347708, 347674, 347689,
//				347539, 347185, 346764, 346075, 345909, 345420, 345388, 345433,
//				345166, 345025, 344542, 344404, 344490, 344075, 343961, 343929,
//				343813, 343638, 343376, 343399, 343127, 343048, 343002, 342892,
//				342811, 342543, 342360, 342237, 342059, 341736, 341577, 341531,
//				341526, 341513, 341059, 340734, 340520, 340526, 339986, 339856,
//				339918, 339548, 338700, 338437, 338216, 338231, 338091, 337928,
//				337636, 337188, 337115, 337275, 336897, 336952, 336862, 336509,
//				336258, 336185, 336082, 335809, 335670, 335502, 335442, 335347,
//				335363, 335371, 335197, 335081, 334687, 334376, 334089, 334132,
//				334186, 333691, 333561, 333062, 332491, 332333, 332321, 332051,
//				330691, 328960, 328175, 327553, 325534, 324263, 322294, 322017,
//				321881, 321680, 321522, 320968, 320897, 320833, 320515, 320147,
//				320235, 320113, 319664, 319631, 319467, 319548, 319428, 319083,
//				319006, 318882, 317953, 317661, 317561, 317346, 317263, 317109,
//				317134, 316818, 316557, 316325, 316246, 316166, 315868, 315729,
//				315543, 315378, 315206, 314584, 313634, 313391, 313360, 313259,
//				313233, 313030, 312804, 312310, 312184, 311074, 310644, 310632,
//				310582, 310496, 310500, 310567, 310452, 310412, 310314, 310315,
//				309880, 309894, 309638, 309445, 309353, 308870, 307985, 307967,
//				307273, 306900, 306601, 306460, 306451, 306360, 305900, 305633,
//				304928, 304726, 304384, 304195, 304127, 304241, 303975, 303824,
//				303452, 303316, 303101, 303001, 302900, 302560, 302513, 302430,
//				302182, 301985, 301746, 300971, 300931, 300485, 300089, 299894,
//				299928, 299506, 299477, 299372, 299191, 298924, 298910, 298829,
//				298663, 298514, 298419, 298116, 297645, 297611, 297428, 297175,
//				297065, 296951, 297079, 296916, 296900, 296548, 296560, 296538,
//				296443, 296119, 296038, 295939, 295843, 295831, 295444, 294956,
//				294645, 294258, 293033, 292888, 291490, 291472, 291421, 291239,
//				291087, 290963, 290415, 290270, 290031, 290010, 289938, 289647,
//				289348, 289182, 288443, 288522, 288225, 287692, 287833, 287506,
//				287300, 286350, 284963, 284909, 284810, 284409, 284270, 284233,
//				283946, 283734, 283496, 283312, 282849, 282800, 282694, 282541,
//				281742, 281760, 281650, 281486, 281240, 281039, 283507, 280376,
//				279779, 279333, 278822, 278811, 278752, 278477, 277723, 277848,
//				277698, 277626, 277474, 277444, 277130, 276940, 276253, 276154,
//				275776, 275670, 275729, 275444, 274899, 274866, 274786, 274672,
//				274565, 274249, 274208, 273981, 273906, 273845, 273668, 273205,
//				273860, 273071, 273020, 272941, 272632, 272628, 272613, 272603,
//				272229, 271819, 271771, 271089, 271014, 270975, 270839, 270032,
//				270007, 269899, 269133, 269116, 268976, 268788, 268276, 267835,
//				267598, 265919, 265773, 265712, 265285, 265203, 265175, 264846,
//				264423, 264304, 263637, 262716, 262458, 262079, 261777, 260983,
//				260816, 260316, 260235, 260131, 260148, 260092, 260077, 259644,
//				259448, 259365, 259372, 259005, 258905, 258889, 258844, 258109,
//				257718, 257284, 256436, 256325, 255614, 255233, 255226, 254980,
//				254561, 253601, 253537, 253370, 253329, 253364, 253333, 253092,
//				253041, 252752, 252473, 251212, 250629, 249092, 247872, 247516,
//				247298, 246621, 246170, 246110, 245881, 245339, 245211, 244652,
//				244209, 243923, 243772, 243648, 243402, 242152, 241833, 241775,
//				241534, 241485, 241028, 240783, 240762, 239804, 239425, 239331,
//				239001, 238952, 238830, 237572, 237146, 236795, 236569, 236478,
//				236204, 235977, 235735, 235563, 235283, 235056, 234470, 233615,
//				233030, 232252, 231781, 231693, 231516, 231404, 231107, 230892,
//				230488, 230405, 230290, 230263, 230131, 229944, 229575, 229420,
//				228935, 228709, 228576, 227981, 226871, 226828, 226831, 226479,
//				226469, 226190, 226134, 225579, 225581, 225418, 225304, 224956,
//				224920, 224659, 224635, 224569, 224433, 224099, 223802, 223580,
//				223454, 223022, 222905, 222749, 222575, 222049, 222024, 222015,
//				221776, 221066, 219178, 219133, 218343, 218049, 217907, 217034,
//				216855, 216253, 216251, 216005, 215275, 215195, 214748, 214743,
//				214309, 214254, 213352, 213262, 213024, 212937, 212794, 212183,
//				211945, 211440, 211319, 210552, 210229, 210045, 210488, 209357,
//				209203, 208786, 208772, 208713, 208645, 208711, 207948, 207746,
//				207735, 207667, 207180, 206934, 206535, 205859, 205270, 205070,
//				205004, 204049, 203861, 203797, 203746, 203171, 202968, 202559,
//				202335, 201640, 201333, 201152, 201025, 200882, 200779, 200279,
//				199779, 198510, 198442, 198146, 197828, 197814, 197724, 197674,
//				197635, 197488, 196984, 196697, 196563, 196603, 196273, 196092,
//				195611, 195161, 195091, 195118, 194636, 194337, 194095, 193961,
//				192284, 192138, 191760, 191279, 190598, 190440, 189709, 189824,
//				189432, 188015, 187438, 186352, 186526, 185688, 185431, 184798,
//				184603, 184289, 183911, 183325, 183306, 182394, 182325, 182200,
//				182209, 182112, 181949, 181847, 181730, 181566, 181460, 181381,
//				180372, 178734, 178524, 177927, 177857, 177568, 177401, 177389,
//				177232, 177019, 176322, 175959, 175813, 175770, 175505, 175465,
//				175278, 175043, 174818, 174520, 174309, 173920, 173734, 173140,
//				171637, 170898, 169783, 169508, 169187, 168822, 168734, 168628,
//				168376, 168103, 167699, 166955, 166726, 166265, 165605, 165193,
//				164637, 164358, 164349, 164298, 164101, 164058, 163957, 163895,
//				163613, 163567, 163504, 162699, 162313, 161993, 161868, 162026,
//				160919, 160896, 159709, 159542, 158838, 158727, 158709, 158703,
//				158329, 158239, 158243, 157814, 157512, 156769, 156544, 156208,
//				156069, 155623, 155266, 154903, 154883, 154621, 154541, 154484,
//				154311, 154015, 153716, 153659, 152562, 152446, 151823, 151341,
//				151311, 151052, 150899, 150696, 150517, 150100, 150006, 149342,
//				149158, 148674, 148632, 148386, 147629, 146949, 146227, 146135,
//				144855, 144309, 144293, 144034, 143329, 143185, 142784, 142391,
//				142300, 142151, 142102, 142074, 141523, 141481, 141356, 141178,
//				141137, 141109, 139987, 138827, 138748, 138558, 138416, 137953,
//				137868, 137807, 137441, 137448, 137009, 136869, 136624, 136667,
//				135863, 135745, 135088, 134298, 132465, 132382, 132223, 131438,
//				131158, 130779, 130692, 130597, 130147, 129853, 129679, 129582,
//				129503, 129395, 129335, 129349, 129046, 128902, 128783, 127808,
//				127618, 126515, 126094, 125956, 125415, 124859, 124503, 124272,
//				124266, 124213, 123968, 123971, 123437, 122923, 122311, 120738,
//				119101, 119036, 118674, 118619, 118583, 117156, 116994, 116781,
//				116727, 116648, 115955, 115611, 114748, 114686, 114021, 114646,
//				114389, 114351, 114166, 113936, 113684, 113691, 112565, 112154,
//				111907, 111900, 111285, 109782, 109685, 109671, 109687, 109332,
//				108893, 108792, 108719, 108674, 108450, 108438, 108325, 108207,
//				108128, 108063, 107948, 108000, 107578, 106759, 106390, 106294,
//				105995, 105836, 105695, 105226, 105209, 104500, 104361, 102883,
//				102559, 102066, 101565, 101507, 101560, 101441, 101428, 100973,
//				100675, 100660, 100652, 100293, 100017, 99713, 99563, 99254,
//				99089, 98971, 98947, 98838, 98818, 97531, 97133, 96107, 96003,
//				95907, 95450, 95219, 95187, 95102, 94955, 94760, 94227, 94025,
//				93920, 93142, 92759, 92446, 92334, 91782, 91665, 91343, 91204,
//				91172, 90873, 89122, 88972, 88846, 88797, 88370, 88264, 87000,
//				86723, 85972, 85748, 85185, 84695, 84516, 84276, 83547, 81994,
//				81339, 81072, 80766, 80639, 80541, 79461, 79162, 79058, 79038,
//				78921, 77939, 77606, 77589, 76479, 76307, 76002, 75750, 74892,
//				74854, 74732, 74698, 73319, 73067, 72581, 72253, 71894, 71725,
//				71596, 70784, 70703, 70502, 70485, 70460, 70451, 70368, 70276,
//				70072, 69861, 69633, 69625, 69584, 69232, 69143, 69102, 68159,
//				68119, 67960, 67954, 67899, 67824, 67626, 67598, 67199, 65825,
//				65245, 65390, 65249, 64998, 64807, 58033, 55222, 54326, 54207,
//				53853, 53578, 52990, 50633, 49972, 49686, 46110, 46002, 45612,
//				45087, 43894, 43804, 43541, 43205, 42955, 41938, 41796, 41656,
//				41516, 41479, 41325, 41314, 41278, 41224, 41203, 41182, 41189,
//				41178, 41173, 41163, 41132, 40979, 40713, 40195, 37481, 36560,
//				35932, 35837, 35833, 35272, 35116, 35047, 34502, 33867, 33274,
//				32558, 32101, 31764, 31680, 30271, 28435, 28413, 28295, 27775,
//				27692, 27174, 26776, 26757, 26636, 26571, 26324, 26315, 26295,
//				26263, 26209, 26149, 26045, 26043, 25980, 25758, 25684, 25671,
//				25622, 25511, 25041, 25011, 24046, 23908, 23674, 23565, 23548,
//				23403, 23360, 23351, 23301, 23296, 23175, 23116, 23098, 22862,
//				22790, 22365, 19511, 19189, 18919, 18843, 18360, 18245, 17874,
//				17448, 15287, 14228, 12174, 11530, 11327, 11103, 10892, 10802,
//				9443, 9157, 8635, 8323, 8252, 8088, 8049, 7954, 7708, 7688,
//				7588, 7504, 7432, 7266, 6442, 6318, 6256, 6022, 5935 };
		
		
//		int[] a={
//				419069,
//				418008,
//				417250,
//				417207,
//				413426,
//				413140,
//				394704};
//		for (int i : a) {
//			params.put("datas", "{\"process\":\"sub\",\"processinstanceid\":\""
//					+ i + "\",\"from\":\"back\"}");// 关闭流程
//			String result = HttpClientUtil
//					.doGet("http://119.29.37.183:8080/workflow/process/abortProcessById",
//							params);
//		}

		// params.put("datas",
		// "{\"process\":\"sub\",\"processinstanceid\":\""
		// + 334816
		// + "\",\"from\":\"back\"}");// 关闭流程
		// String result = HttpClientUtil.doGet(
		// "http://119.29.37.183:8080/workflow/process/abortProcessById",
		// params);
		// // Map<String,String> par = new HashMap<String,String>();
		// // Map<String,Object> params = new HashMap<String,Object>();
		// // params.put("userid", "admin");
		// // params.put("processinstanceid", 207545);
		// // params.put("taskName","二次支付确认");
		// // params.put("result", "3");
		// // JSONObject bo= JSONObject.fromObject(params);
		// // par.put("datas",bo.toString());
		// // System.out.println(par.toString());
		// // String message =
		// HttpClientUtil.doGet("http://119.29.37.183:8080/workflow"+"/process/completeSubTask",
		// par);
		// //
		// // Map<String,String> par = new HashMap<String,String>();
		// // String path = "http://119.29.37.183:8080/workflow" +
		// "/process/completeSubTask";
		// // Map<String, Object> map = new HashMap<String, Object>();
		// // map.put("userid", "admin");// 轮询节点写死
		// // map.put("processinstanceid", 244034);// 子流程节点
		// // map.put("taskName", "轮询");
		// // Map<String, Object> data = new HashMap<String, Object>();
		// // // 人工核保流向轮询节点标记
		// // data.put("result", "1");// 1失败，3成功
		// // map.put("data", data);
		// // JSONObject jsonObject = JSONObject.fromObject(map);
		// // par.put("datas", jsonObject.toString());
		// // HttpClientUtil.doGet(path, par);
		//
	}

	// @Test
	// public void test(){
	// insbwt.inWorkTime(new Date(), "1244191079");
	// Map<String,String> queryUrlMap = new HashMap<String,String>();
	// queryUrlMap.put("deptid","1251191001");
	// queryUrlMap.put("providerid","201651");
	// queryUrlMap.put("quotetype", "01");
	// queryUrlMap.put("conftype","02");
	// List<Object> targetUrl =
	// insbAutoconfigService.getEpathByAutoConfig4New(queryUrlMap);

	// String deptid ="1251191001";//出单网点
	// Map<String, Object> deptmap = new HashMap<String, Object>();
	// deptmap.put("providerid", "201651");
	// // deptmap.put("deptid", deptid);
	// deptmap.put("conftype", "02");

	// List<String> tempDeptIds =
	// insbAutoconfigshowService.getParentDeptIds4Show(deptid);
	// deptmap.put("deptList", tempDeptIds);
	// System.out.println(deptmap);
	// List<String> quotetypes =
	// insbAutoconfigshowService.queryByProId(deptmap);
	// System.out.println(quotetypes);

	// }

	// public void test23(){
	// // INSBQuotetotalinfo insbQuotetotalinfo = new INSBQuotetotalinfo();
	// // insbQuotetotalinfo.setTaskid("97116");
	// // INSBQuotetotalinfo result =
	// insbQuotetotalinfoDao.selectOne(insbQuotetotalinfo);
	// // System.out.println("-------"+result.getSourceFrom());
	// Map<String,Object> map = new HashMap<String,Object>();
	// map.put("taskid", "97283");
	// map.put("inscomcode", "201644");
	// INSBOrder temp = insbOrderDao.selectOrderByTaskId(map);
	// temp.setOrderstatus("1");
	// insbOrderDao.updateById(temp);
	// }

	// public void testC1(){
	// Date startD = new Date();
	// Long start = startD.getTime();
	// workflowmainService.getQuoteTypeOld("37393l", "201644", "0");
	// Date endd = new Date();
	// Long end = endd.getTime();
	// System.out.println(end-start);
	// }
	// @Test
	// public void testC(){
	// for (int i = 0; i < 10; i++) {
	// Thread t = new Thread(new Runnable() {
	// @Override
	// public void run() {
	// Date startD = new Date();
	// Long start = startD.getTime();
	// // workflowmainService.getQuoteTypeOld("37393l", "201644", "0");
	// Date endd = new Date();
	// Long end = endd.getTime();
	// System.out.println(end-start);
	//
	// }
	// });
	// t.start();
	// }
	//
	// }
	//
	//
	// // @Test
	// public void test5(){
	// System.out.println(agtService.getAgentData("admin","620515650"));
	// }
	//
	// // @Test
	// public void test4(){
	// //taskid=26401,inscomcode=20074401====参数：子流程_subtaskId=26408,新车购置价_taxPrice=73826.0,不含税价_price=70800.0,含税类比价_analogyTaxPrice=0.0,不含税类比价_analogyPrice=0.0,初等日期_regDate=2016-04-06,座位数_seats=5车型名称_modelName=江淮HFC7151EATV轿车
	// interf.getCarModelInfo("26408", "26401", "20074401",
	// 73826.0,
	// 70800.0,
	// 0.0,
	// 0.0,
	// "2016-04-06",
	// 5,
	// "江淮HFC7151EATV轿车",
	// "72300");
	// }
	//
	// // @Test
	// public void test3(){
	// INSBWorkflowsubtrack
	// insbWorkflowsubtrack=dao.findPrevWorkFlowSub("25041", "20");
	// System.out.println(insbWorkflowsubtrack);
	// }
	// // @Test
	// /**
	// * 调用 规则 校验
	// */
	// @Test
	public void test() {
		String a = appQuotationService.getQuotationValidatedInfo("", "187302",
				"200737");
		System.out.println(a);
	}

	//
	//
	// // @Test
	public void test2() {
		String a = appQuotationService.getPriceInterval("", "26401", "201644",
				89000D, 89000D, 89000D, 89000D, "2015-05-11", 5);
		System.out.println(a);
	}

	//
	//
	// public static void main(String[] args) {
	// for (int i = 0; i < 300; i++) {
	// Thread t = new Thread(new Runnable() {
	// @Override
	// public void run() {
	// String path =
	// "http://localhost:8080/workflow/process/startWorkflowProcess";
	// Map<String, String> map = new HashMap<String, String>();
	// map.put("renewal", "0");
	// String result = HttpClientUtil.doGet(path, map);
	// //
	// // Map<String, Object> rj =
	// CifMakeOrderNoTest.getMapFromJsonString(result);
	// // String path2 =
	// "http://115.159.237.154:8080/workflow//process/completeMessageInput";
	// // String mainid =rj.get("processinstanceid").toString();
	// // List<String> incoids = new ArrayList<String>();
	// // incoids.add("2025");
	// // incoids.add("20335");
	// // incoids.add("2045");
	// // incoids.add("255555");
	// // incoids.add("20251");
	// // incoids.add("203351");
	// // incoids.add("20451");
	// // incoids.add("2555551");
	// // System.out.println(mainid);
	// // Map<String, Object> map2 = new HashMap<String, Object>();
	// // map2.put("userId", "admin");
	// // map2.put("processinstanceid", Long.parseLong(mainid));
	// // map2.put("incoids", incoids);
	// // map2.put("renewal", "0");
	// // JSONObject jsonObject = JSONObject.fromObject(map2);
	// // Map<String, String> params = new HashMap<String, String>();
	// // params.put("datas", jsonObject.toString());
	// // String re= HttpClientUtil.doGet(path2, params);
	// // System.out.println(re);
	// }
	// });
	// t.start();
	// }
	// // String path1 =
	// "http://localhost:8080/workflow/process/startWorkflowProcess";
	// // Map<String, String> map2 = new HashMap<String, String>();
	// // map2.put("renewal", "0");
	// // String result = HttpClientUtil.doGet(path1, map2);
	// // String path = "http://localhost:8080/workflow" +
	// "/process/completeSubTask";
	// // Map<String, Object> map1 = new HashMap<String, Object>();
	// // map1.put("result", "");
	// //
	// // Map<String, Object> map = new HashMap<String, Object>();
	// // map.put("data", map1);
	// // map.put("userid", "");
	// // map.put("taskName", "人工规则报价");
	// // map.put("processinstanceid", 44972l);
	// //
	// // JSONObject jsonObject = JSONObject.fromObject(map);
	// // Map<String, String> params = new HashMap<String, String>();
	// // params.put("datas", jsonObject.toString());
	// // System.out.println(params);
	// // HttpClientUtil.doGet(path, params);
	//
	// }
	// public static Map<String, Object> getMapFromJsonString(String jsonString)
	// {
	// JSONObject jsonObject = JSONObject.fromObject(jsonString);
	// Iterator<?> keyIter = jsonObject.keys();
	// String key;
	// Object value;
	// Map<String, Object> valueMap = new HashMap<String, Object>();
	// while (keyIter.hasNext()) {
	// key = (String) keyIter.next();
	// value = jsonObject.get(key);
	// valueMap.put(key, value);
	// }
	// return valueMap;
	// }

}
