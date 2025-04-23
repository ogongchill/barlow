import http from 'k6/http';
import {sleep, check, group} from 'k6';
import {SharedArray} from 'k6/data'

const [secret] = new SharedArray('secret', () => {
    return [JSON.parse(open('../common-secret.json'))]
});
const BASE_URL = secret.baseUrl;
const SLEEP_DURATION = 0.1;

/**
 * 가상의 사용자 1000 명을 지정하여 [최근법안전체조회 API] 테스트
 * 1000 명의 사용자가 무작위의 필터를 사용해서 조회하는 Load Test 수행
 */
export const options = {
    stages: [
        {duration: '5m', target: 1000}, // 5분 동안 VU를 1000명까지 점진적으로 증가
        {duration: '10m', target: 1000}, // // 10분 동안 1000명 유지
        {duration: '5m', target: 0}, // 5분 동안 점진적으로 종료
    ],
    thresholds: {
        http_req_duration: ['p(99)<500'], // 99% 요청은 500ms 이하
        http_req_failed: ['rate<0.01'],   // 실패율 1% 이하
    }
};

export default function () {
    group("/api/v1/recent-bill/thumbnail", () => {
        const params = {
            headers: {
                Authorization: `Bearer ${secret.token}`,
                "Content-Type": "application/json",
                Accept: "application/json",
                "X-Client-OS": `${secret.clientOs}`,
                "X-Client-OS-Version": `${secret.clientOsVersion}`,
                "X-Device-ID": `${secret.deviceId}`,
            }
        }
        const random = Math.random();
        const filters = {
            page: Math.floor(random * 10).toString(),
            size: [10, 20, 30, 40, 50][Math.floor(random * 5)].toString(),
            legislationType: ["HOUSE_STEERING", "LEGISLATION_AND_JUDICIARY", "STRATEGY_AND_FINANCE", "NATIONAL_DEFENSE"][Math.floor(random * 4)].toString(),
            progressStatus: ["COMMITTEE_RECEIVED", "PLENARY_SUBMITTED", "PLENARY_DECIDED", "PROMULGATED"][Math.floor(random * 4)].toString(),
            partyName: ["PEOPLE_POWER", "MINJOO", "PROGRESSIVE", "NEW_FUTURE"][Math.floor(random * 4)].toString()
        }
        const query = buildQueryString(filters);
        const url = `${BASE_URL}/api/v1/recent-bill/thumbnail?${query}`;

        const res = http.get(url, params);
        check(res, {"status is 200": (res) => res.status === 200});
        sleep(SLEEP_DURATION);
    })
}

function buildQueryString(params) {
    return Object.entries(params)
        .map(([key, value]) => `${encodeURIComponent(key)}=${encodeURIComponent(value)}`)
        .join("&");
}
