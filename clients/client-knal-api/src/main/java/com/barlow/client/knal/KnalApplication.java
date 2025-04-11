// package com.barlow.client.knal;
//
// import org.springframework.boot.ApplicationRunner;
// import org.springframework.boot.SpringApplication;
// import org.springframework.boot.autoconfigure.SpringBootApplication;
// import org.springframework.context.annotation.Bean;
//
// import com.barlow.client.knal.opencongress.api.common.DefaultRequest;
// import com.barlow.client.knal.opencongress.api.preannounce.PreAnnouncementResponse;
// import com.barlow.client.knal.opendata.api.NationalAssemblyLegislationOpenDataApi;
// import com.barlow.client.knal.opendata.api.request.BillInfoListRequest;
// import com.barlow.client.knal.opendata.api.request.BillPetitionMemberListRequest;
// import com.barlow.client.knal.opendata.api.request.BillPreliminaryExaminationInfoRequest;
// import com.barlow.client.knal.opendata.api.response.BillInfoListResponse;
// import com.barlow.client.knal.opendata.api.response.BillPetitionMemberListResponse;
// import com.barlow.client.knal.opendata.api.response.BillPreliminaryExaminationInfoResponse;
//
// @SpringBootApplication
// public class KnalApplication {
// 	// client-knal-api
// 	private final NationalAssemblyLegislationOpenDataApi api;
//
// 	public KnalApplication(NationalAssemblyLegislationOpenDataApi api) {
// 		this.api = api;
// 	}
//
// 	@Bean
// 	ApplicationRunner init() {
// 		return args -> {
// 			BillInfoListRequest request = BillInfoListRequest.builder()
// 				.startProposeDate("2025-04-10")
// 				.endProposeDate("2025-04-10")
// 				.startOrdinal(22)
// 				.endOrdinal(22)
// 				.numOfRows(100)
// 				.pageNo(1)
// 				.build();
// 			BillInfoListResponse response = api.getBillInfoList(request);
// 			System.out.println(response.toStringForLog());
// 			System.out.println("========================");
//
// 			BillPetitionMemberListRequest request1 = BillPetitionMemberListRequest.builder()
// 				.billId("PRC_G2H5F0F4D0E9D1K8L2J3K0I5J0H5I3")
// 				.gbn1("bill")
// 				.gbn2("reception")
// 				.build();
// 			BillPetitionMemberListResponse response1 = api.getBillPetitionMemberList(request1);
// 			System.out.println(response1.toStringForLog());
// 			System.out.println("========================");
//
// 			BillPreliminaryExaminationInfoRequest request2 = BillPreliminaryExaminationInfoRequest.builder()
// 				.billId("PRC_Z2X5Y0G4H0F8F0E8E5D0L4L2J1K2J5")
// 				.build();
// 			BillPreliminaryExaminationInfoResponse response2 = api.getBillPreliminaryExaminationInfo(request2);
// 			System.out.println(response2.toStringForLog());
// 		};
// 	}
//
// 	public static void main(String[] args) {
// 		SpringApplication.run(KnalApplication.class, args);
// 	}
// }
