import http from 'k6/http';
import {sleep, check, group} from 'k6';
import {SharedArray} from 'k6/data'

const [secret] = new SharedArray('secret', () => {
    return [JSON.parse(open('../common-secret.json'))]
});
const BASE_URL = secret.baseUrl;
const SLEEP_DURATION = 0.1;

/**
 * [소관위전체조회 -> 특정 소관위원회 프로필 조회] 시나리오 테스트
 * 여러 소관위원회들에 대한 정보를 지속적으로 조회하는 load 테스트로 진행한다
 */
export const options = {
    stages: [
        {duration: '5m', target: 1000},
        {duration: '10m', target: 1000},
        {duration: '5m', target: 0},
    ],
    thresholds: {
        http_req_duration: ['p(99)<500'],
        http_req_failed: ['rate<0.01'],
    }
};

export default function () {
    retrieveCommittee();
    retrieveProfile();
}

function retrieveCommittee() {
    const url = `${BASE_URL}/api/v1/legislation-accounts/committees/info`;
    const res = http.get(url, getRequestParams())
    check(res, {"status is 200": (res) => res.status === 200});
    sleep(SLEEP_DURATION);
}

function retrieveProfile() {
    const legislationType = [
        "HOUSE_STEERING", "LEGISLATION_AND_JUDICIARY", "NATIONAL_POLICY", "STRATEGY_AND_FINANCE",
        "EDUCATION", "SCIENCE_ICT_BROADCASTING_AND_COMMUNICATIONS", "FOREIGN_AFFAIRS_AND_UNIFICATION",
        "NATIONAL_DEFENSE", "PUBLIC_ADMINISTRATION_AND_SECURITY", "CULTURE_SPORTS_AND_TOURISM",
        "AGRICULTURE_FOOD_RURAL_AFFAIRS_OCEANS_AND_FISHERIES", "TRADE_INDUSTRY_ENERGY_SMES_AND_STARTUPS",
        "HEALTH_AND_WELFARE", "ENVIRONMENT_AND_LABOR", "LAND_INFRASTRUCTURE_AND_TRANSPORT",
        "INTELLIGENCE", "GENDER_EQUALITY_FAMILY", "SPECIAL_COMMITTEE_ON_BUDGET_ACCOUNTS"
    ][Math.floor(Math.random() * 18)].toString();
    const url = `${BASE_URL}/api/v1/legislation-accounts/${legislationType}/profile`;
    const res = http.get(url, getRequestParams())
    check(res, {"status is 200": (res) => res.status === 200});
    sleep(SLEEP_DURATION);
}

function getRequestParams() {
    return {
        headers: {
            Authorization: `Bearer ${secret.token}`,
            'Content-Type': 'application/json',
            Accept: 'application/json',
            'X-Client-OS': secret.clientOs,
            'X-Client-OS-Version': secret.clientOsVersion,
            'X-Device-ID': secret.deviceId,
        }
    };
}