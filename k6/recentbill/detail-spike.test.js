import http from 'k6/http';
import {sleep, check, group} from 'k6';
import {SharedArray} from 'k6/data'

const [secret] = new SharedArray('secret', () => {
    return [JSON.parse(open('../common-secret.json'))]
});
const BASE_URL = secret.baseUrl;
const SLEEP_DURATION = 0.1;

/**
 * 하나의 법안에 순간적으로 많은 트래픽이 몰리는 상황
 * 예를 들어, 링크 공유를 통해서 특정 법안이 주목을 받아 트래픽이 몰리게 될 수 있음
 */
export const options = {
    stages: [
        {duration: '2m', target: 1000}, // fast ramp-up to a high point
        {duration: '1m', target: 0}, // quick ramp-down to 0 users
    ],
    thresholds: {
        http_req_duration: ['p(99)<500'], // 99% 요청은 500ms 이하
        http_req_failed: ['rate<0.01'],   // 실패율 1% 이하
    }
}

export default function () {
    group("/api/v1/recent-bill/detail", () => {
        const headers = {
            headers: {
                Authorization: `Bearer ${secret.token}`,
                "Content-Type": "application/json",
                Accept: "application/json",
                "X-Client-OS": `${secret.clientOs}`,
                "X-Client-OS-Version": `${secret.clientOsVersion}`,
                "X-Device-ID": `${secret.deviceId}`,
            }
        }
        const pathParams = secret.billId;
        const url = `${BASE_URL}/api/v1/recent-bill/detail/${pathParams}`;

        const res = http.get(url, headers);
        check(res, {"status is 200": (res) => res.status === 200});
        sleep(SLEEP_DURATION);
    })
}