package com.zzb.cif;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

import com.cninsure.system.dao.INSCCodeDao;
import com.cninsure.system.dao.INSCDeptDao;
import com.cninsure.system.dao.INSCMenuDao;
import com.cninsure.system.dao.INSCMessageDao;
import com.cninsure.system.dao.INSCRoleDao;
import com.cninsure.system.dao.INSCRoleMenuDao;
import com.cninsure.system.dao.INSCScheduleDao;
import com.cninsure.system.dao.INSCTaskDao;
import com.cninsure.system.dao.INSCUserDao;
import com.cninsure.system.dao.INSCUserRoleDao;
import com.cninsure.system.entity.INSCCode;
import com.cninsure.system.entity.INSCDept;
import com.cninsure.system.entity.INSCMenu;
import com.cninsure.system.entity.INSCMessage;
import com.cninsure.system.entity.INSCRole;
import com.cninsure.system.entity.INSCRoleMenu;
import com.cninsure.system.entity.INSCSchedule;
import com.cninsure.system.entity.INSCTask;
import com.cninsure.system.entity.INSCUser;
import com.cninsure.system.entity.INSCUserRole;
import com.zzb.cm.dao.INSBApplicantDao;
import com.zzb.cm.dao.INSBApplicanthisDao;
import com.zzb.cm.dao.INSBCarconfigDao;
import com.zzb.cm.dao.INSBCarinfoDao;
import com.zzb.cm.dao.INSBCarinfohisDao;
import com.zzb.cm.dao.INSBCarkindpriceDao;
import com.zzb.cm.dao.INSBCarmodelinfoDao;
import com.zzb.cm.dao.INSBCarmodelinfohisDao;
import com.zzb.cm.dao.INSBCarowneinfoDao;
import com.zzb.cm.dao.INSBCorecodemapDao;
import com.zzb.cm.dao.INSBCoreriskmapDao;
import com.zzb.cm.dao.INSBDeliveryaddressDao;
import com.zzb.cm.dao.INSBFilebusinessDao;
import com.zzb.cm.dao.INSBFilelibraryDao;
import com.zzb.cm.dao.INSBFlowinfoDao;
import com.zzb.cm.dao.INSBFlowlogsDao;
import com.zzb.cm.dao.INSBImagelibraryDao;
import com.zzb.cm.dao.INSBInsuredDao;
import com.zzb.cm.dao.INSBInsuredhisDao;
import com.zzb.cm.dao.INSBLegalrightclaimDao;
import com.zzb.cm.dao.INSBLegalrightclaimhisDao;
import com.zzb.cm.dao.INSBOrderDao;
import com.zzb.cm.dao.INSBPersonDao;
import com.zzb.cm.dao.INSBPlatcarpriceDao;
import com.zzb.cm.dao.INSBQuoteinfoDao;
import com.zzb.cm.dao.INSBQuotetotalinfoDao;
import com.zzb.cm.dao.INSBRelationpersonDao;
import com.zzb.cm.dao.INSBRelationpersonhisDao;
import com.zzb.cm.dao.INSBSpecifydriverDao;
import com.zzb.cm.dao.INSBSpecifydriverhisDao;
import com.zzb.cm.dao.INSBSupplementaryinfoDao;
import com.zzb.cm.dao.INSBUserremarkDao;
import com.zzb.cm.dao.INSBUserremarkhisDao;
import com.zzb.cm.entity.INSBApplicant;
import com.zzb.cm.entity.INSBApplicanthis;
import com.zzb.cm.entity.INSBCarconfig;
import com.zzb.cm.entity.INSBCarinfo;
import com.zzb.cm.entity.INSBCarinfohis;
import com.zzb.cm.entity.INSBCarkindprice;
import com.zzb.cm.entity.INSBCarmodelinfo;
import com.zzb.cm.entity.INSBCarmodelinfohis;
import com.zzb.cm.entity.INSBCarowneinfo;
import com.zzb.cm.entity.INSBCorecodemap;
import com.zzb.cm.entity.INSBCoreriskmap;
import com.zzb.cm.entity.INSBDeliveryaddress;
import com.zzb.cm.entity.INSBFilebusiness;
import com.zzb.cm.entity.INSBFilelibrary;
import com.zzb.cm.entity.INSBFlowinfo;
import com.zzb.cm.entity.INSBFlowlogs;
import com.zzb.cm.entity.INSBImagelibrary;
import com.zzb.cm.entity.INSBInsured;
import com.zzb.cm.entity.INSBInsuredhis;
import com.zzb.cm.entity.INSBLegalrightclaim;
import com.zzb.cm.entity.INSBLegalrightclaimhis;
import com.zzb.cm.entity.INSBOrder;
import com.zzb.cm.entity.INSBPerson;
import com.zzb.cm.entity.INSBPlatcarprice;
import com.zzb.cm.entity.INSBQuoteinfo;
import com.zzb.cm.entity.INSBQuotetotalinfo;
import com.zzb.cm.entity.INSBRelationperson;
import com.zzb.cm.entity.INSBRelationpersonhis;
import com.zzb.cm.entity.INSBSpecifydriver;
import com.zzb.cm.entity.INSBSpecifydriverhis;
import com.zzb.cm.entity.INSBSupplementaryinfo;
import com.zzb.cm.entity.INSBUserremark;
import com.zzb.cm.entity.INSBUserremarkhis;
import com.zzb.conf.dao.INSBPolicyitemDao;
import com.zzb.conf.entity.INSBPolicyitem;
import com.zzb.mobile.dao.INSBPayResultDao;

@WebAppConfiguration(value = "src/main/webapp")
@ContextConfiguration(locations = { "classpath:config/spring-config.xml",
		"classpath:config/spring-security-config.xml",
		"classpath:config/spring-mvc-config.xml", })
public class SelectOneTest {
	@Resource
	INSCScheduleDao inscSdao;
	@Resource
	INSCTaskDao inscTdao;
	@Resource
	INSCCodeDao inscCdao;
	@Resource
	INSCDeptDao insCDdao;
	@Resource
	INSCMenuDao inscMdao;
	@Resource
	INSCMessageDao inscMsdao;
	@Resource
	INSCRoleDao inscRdao;
	@Resource
	INSCRoleMenuDao inscRMdao;
	@Resource
	INSCUserDao inscUdao;
	@Resource
	INSCUserRoleDao inscURdao;
	@Resource
	INSBApplicantDao insbAdao;
	@Resource
	INSBApplicanthisDao insbATdao;
	@Resource
	INSBCarconfigDao insbCFdao;
	@Resource
	INSBCarinfohisDao insbCHdao;
	@Resource
	INSBCarinfoDao insbCHodao;
	@Resource
	INSBCarkindpriceDao insbCKPdao;
	@Resource
	INSBCarmodelinfoDao insbCMdao;
	@Resource
	INSBCarmodelinfohisDao insbCMHdao;
	@Resource
	INSBCarowneinfoDao insbCMWdao;
	@Resource
	INSBCorecodemapDao insbCCdao;
	@Resource
	INSBCoreriskmapDao insbCRMdao;
	@Resource
	INSBDeliveryaddressDao insbDLdao;
	@Resource
	INSBFilebusinessDao insbFBdao;
	@Resource
	INSBFilelibraryDao insbFLdao;
	@Resource
	INSBFlowinfoDao insbFDao;
	@Resource
	INSBFlowlogsDao insbFLODao;
	@Resource
	INSBImagelibraryDao insbIDao;
	@Resource
	INSBInsuredhisDao insIHdao;
	@Resource
	INSBInsuredDao insIdao;
	@Resource
	INSBLegalrightclaimhisDao insbLRCHdao;
	@Resource
	INSBLegalrightclaimDao insbLRCdao;
	@Resource
	INSBOrderDao insbOdao;
	@Resource
	INSBPersonDao insbPOdao;
	@Resource
	INSBPlatcarpriceDao insbPCdao;
	@Resource
	INSBPolicyitemDao insbPLdao;
	@Resource
	INSBQuoteinfoDao insbQdao;
	@Resource
	INSBQuotetotalinfoDao insbQTdao;
	@Resource
	INSBRelationpersonhisDao insbRPHdao;
	@Resource
	INSBRelationpersonDao insbRPdao;
	@Resource
	INSBSpecifydriverhisDao insbSHdao;
	@Resource
	INSBSpecifydriverDao insbSdao;
	@Resource
	INSBSupplementaryinfoDao insbSUdao;
	@Resource
	INSBUserremarkhisDao insbUHdao;
	@Resource
	INSBUserremarkDao insbUdao;
	@Resource
	INSBPayResultDao insbPRdao;
	@Test
	public void test3(){
		try {
			INSCMessage q = new INSCMessage();
			q.setId("59507730975811e5602acfb905a717c7");
			inscMsdao.selectOne(q);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
//	@Test
	public void test2() {
		
		try {
			INSBUserremark q = new INSBUserremark();
			q.setId("59507730975811e5602acfb905a717c7");
			insbUdao.selectOne(q);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			INSBUserremarkhis q = new INSBUserremarkhis();
			q.setId("59507730975811e5602acfb905a717c7");
			insbUHdao.selectOne(q);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			INSBSupplementaryinfo q = new INSBSupplementaryinfo();
			q.setId("59507730975811e5602acfb905a717c7");
			insbSUdao.selectOne(q);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			INSBSpecifydriver q = new INSBSpecifydriver();
			q.setId("59507730975811e5602acfb905a717c7");
			insbSdao.selectOne(q);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			INSBSpecifydriverhis q = new INSBSpecifydriverhis();
			q.setId("59507730975811e5602acfb905a717c7");
			insbSHdao.selectOne(q);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		try {
			INSBRelationperson q = new INSBRelationperson();
			q.setId("59507730975811e5602acfb905a717c7");
			insbRPdao.selectOne(q);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			INSBRelationpersonhis q = new INSBRelationpersonhis();
			q.setId("59507730975811e5602acfb905a717c7");
			insbRPHdao.selectOne(q);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			INSBQuotetotalinfo q = new INSBQuotetotalinfo();
			q.setId("59507730975811e5602acfb905a717c7");
			insbQTdao.selectOne(q);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			INSBQuoteinfo q = new INSBQuoteinfo();
			q.setId("59507730975811e5602acfb905a717c7");
			insbQdao.selectOne(q);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			INSBPolicyitem q = new INSBPolicyitem();
			q.setId("59507730975811e5602acfb905a717c7");
			insbPLdao.selectOne(q);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			INSBPlatcarprice q = new INSBPlatcarprice();
			q.setId("59507730975811e5602acfb905a717c7");
			insbPCdao.selectOne(q);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			INSBPerson q = new INSBPerson();
			q.setId("59507730975811e5602acfb905a717c7");
			insbPOdao.selectOne(q);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			INSBOrder q = new INSBOrder();
			q.setId("59507730975811e5602acfb905a717c7");
			insbOdao.selectOne(q);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			INSBLegalrightclaim q = new INSBLegalrightclaim();
			q.setId("59507730975811e5602acfb905a717c7");
			insbLRCdao.selectOne(q);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			INSBLegalrightclaimhis q = new INSBLegalrightclaimhis();
			q.setId("59507730975811e5602acfb905a717c7");
			insbLRCHdao.selectOne(q);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			INSBInsured q = new INSBInsured();
			q.setId("59507730975811e5602acfb905a717c7");
			insIdao.selectOne(q);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			INSBInsuredhis q = new INSBInsuredhis();
			q.setId("59507730975811e5602acfb905a717c7");
			insIHdao.selectOne(q);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			INSBImagelibrary q = new INSBImagelibrary();
			q.setId("59507730975811e5602acfb905a717c7");
			insbIDao.selectOne(q);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			INSBFlowlogs q = new INSBFlowlogs();
			q.setId("59507730975811e5602acfb905a717c7");
			insbFLODao.selectOne(q);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			INSBFlowinfo q = new INSBFlowinfo();
			q.setId("59507730975811e5602acfb905a717c7");
			insbFDao.selectOne(q);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			INSBFilelibrary q = new INSBFilelibrary();
			q.setId("59507730975811e5602acfb905a717c7");
			insbFLdao.selectOne(q);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			INSBFilebusiness q = new INSBFilebusiness();
			q.setId("59507730975811e5602acfb905a717c7");
			insbFBdao.selectOne(q);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			INSBDeliveryaddress q = new INSBDeliveryaddress();
			q.setId("59507730975811e5602acfb905a717c7");
			insbDLdao.selectOne(q);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			INSBCoreriskmap q = new INSBCoreriskmap();
			q.setId("59507730975811e5602acfb905a717c7");
			insbCRMdao.selectOne(q);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			INSBCorecodemap q = new INSBCorecodemap();
			q.setId("59507730975811e5602acfb905a717c7");
			insbCCdao.selectOne(q);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			INSBCarowneinfo q = new INSBCarowneinfo();
			q.setId("59507730975811e5602acfb905a717c7");
			insbCMWdao.selectOne(q);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			INSBCarmodelinfohis q = new INSBCarmodelinfohis();
			q.setId("59507730975811e5602acfb905a717c7");
			insbCMHdao.selectOne(q);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			INSBCarmodelinfo q = new INSBCarmodelinfo();
			q.setId("59507730975811e5602acfb905a717c7");
			insbCMdao.selectOne(q);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			INSBCarkindprice q = new INSBCarkindprice();
			q.setId("59507730975811e5602acfb905a717c7");
			insbCKPdao.selectOne(q);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			INSBCarinfo q = new INSBCarinfo();
			q.setId("59507730975811e5602acfb905a717c7");
			insbCHodao.selectOne(q);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			INSBCarinfohis q = new INSBCarinfohis();
			q.setId("59507730975811e5602acfb905a717c7");
			insbCHdao.selectOne(q);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			INSBCarconfig q = new INSBCarconfig();
			q.setId("59507730975811e5602acfb905a717c7");
			insbCFdao.selectOne(q);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			INSBApplicanthis q = new INSBApplicanthis();
			q.setId("59507730975811e5602acfb905a717c7");
			insbATdao.selectOne(q);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			INSBApplicant q = new INSBApplicant();
			q.setId("59507730975811e5602acfb905a717c7");
			insbAdao.selectOne(q);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	@Test
	public void test1() {
		try {
			INSCUserRole q = new INSCUserRole();
			q.setId("59507730975811e5602acfb905a717c7");
			inscURdao.selectOne(q);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			INSCUser q = new INSCUser();
			q.setId("59507730975811e5602acfb905a717c7");
			inscUdao.selectOne(q);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			INSCRoleMenu q = new INSCRoleMenu();
			q.setId("59507730975811e5602acfb905a717c7");
			inscRMdao.selectOne(q);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			INSCRole q = new INSCRole();
			q.setId("59507730975811e5602acfb905a717c7");
			inscRdao.selectOne(q);
		} catch (Exception e) {
			e.printStackTrace();
		}

	
		try {
			INSCMenu q = new INSCMenu();
			q.setId("59507730975811e5602acfb905a717c7");
			inscMdao.selectOne(q);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			INSCDept q = new INSCDept();
			q.setId("59507730975811e5602acfb905a717c7");
			insCDdao.selectOne(q);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			INSCSchedule q = new INSCSchedule();
			q.setId("59507730975811e5602acfb905a717c7");
			inscSdao.selectOne(q);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			INSCTask q = new INSCTask();
			q.setId("59507730975811e5602acfb905a717c7");
			inscTdao.selectOne(q);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			INSCCode q = new INSCCode();
			q.setId("59507730975811e5602acfb905a717c7");
			inscCdao.selectOne(q);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}