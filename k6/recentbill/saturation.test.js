import http from 'k6/http';
import {check, group, sleep} from "k6";
import {SharedArray} from "k6/data";

export const options = {
    stages: [
        // { duration: '1m', target: 25 },
        // { duration: '1m', target: 50 },
        // { duration: '1m', target: 75 },
        // { duration: '1m', target: 100 },
        // { duration: '1m', target: 125 },
        // { duration: '1m', target: 150 },
        // { duration: '1m', target: 175 },
        // { duration: '1m', target: 200 },
        // { duration: '3m', target: 0 }, // 종료
        { duration: '1m', target: 5 },
        { duration: '1m', target: 10 },
        { duration: '1m', target: 15 },
        { duration: '1m', target: 20 },
        { duration: '1m', target: 24 },
        { duration: '1m', target: 26 },
        { duration: '1m', target: 28 },
        { duration: '1m', target: 30 },
        { duration: '2m', target: 0 }, // 종료
    ],
    thresholds: {
        http_req_duration: ['p(95)<500'],  // 응답 시간 95%가 500ms 이내
        http_req_failed: ['rate<0.05'],    // 오류율 5% 이하 허용
    }
}

// setup
const [secret] = new SharedArray('secret', () => {
    return [JSON.parse(open('../common-secret.json'))]
});
const BASE_URL = secret.baseUrl;
const SLEEP_DURATION = 0.1;

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
        const filters = {
            page: "0",
            size: "10",
        }
        if (Math.random() < 0.5) { // 50%
            filters.legislationType = ["HOUSE_STEERING", "LEGISLATION_AND_JUDICIARY", "STRATEGY_AND_FINANCE", "NATIONAL_DEFENSE"][Math.floor(Math.random() * 4)];
        }
        if (Math.random() < 0.2) { // 20%
            filters.progressStatus = ["COMMITTEE_RECEIVED", "PLENARY_SUBMITTED", "PLENARY_DECIDED", "PROMULGATED"][Math.floor(Math.random() * 4)];
        }
        if (Math.random() < 0.05) { // 5%
            filters.proposerType = ["GOVERNMENT", "CHAIRMAN", "SPEAKER", "LAWMAKER"][Math.floor(Math.random() * 4)];
        }
        if (Math.random() < 0.7) { // 70%
            filters.partyName = ["PEOPLE_POWER", "MINJOO", "PROGRESSIVE", "NEW_FUTURE"][Math.floor(Math.random() * 4)];
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