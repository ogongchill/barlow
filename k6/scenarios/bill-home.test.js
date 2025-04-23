import http from 'k6/http';
import {sleep, check} from 'k6';
import {SharedArray} from 'k6/data'

const [secret] = new SharedArray('secret', () => {
    return [JSON.parse(open('../common-secret.json'))]
});
const BASE_URL = secret.baseUrl;
const SLEEP_DURATION = 0.1;

/**
 * [알림 도착 -> 법안디테일 조회 -> 메인홈 조회] 시나리오 테스트
 * 알림을 확인한 사용자가 단기간에 몰리는 spike 성 테스트로 진행한다
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
    retrieveDetailBill()
    retrieveHome()
}

function retrieveDetailBill() {
    const pathParams = secret.billId;
    const url = `${BASE_URL}/api/v1/recent-bill/detail/${pathParams}`;
    const res = http.get(url, getRequestParams('bill-detail'))
    check(res, {"status is 200": (res) => res.status === 200});
    sleep(SLEEP_DURATION);
}

function retrieveHome() {
    const url = `${BASE_URL}/api/v1/home`;
    const res = http.get(url, getRequestParams('home'))
    check(res, {"status is 200": (res) => res.status === 200});
    sleep(SLEEP_DURATION);
}

function getRequestParams(endpointTag) {
    return {
        headers: {
            Authorization: `Bearer ${secret.token}`,
            'Content-Type': 'application/json',
            Accept: 'application/json',
            'X-Client-OS': secret.clientOs,
            'X-Client-OS-Version': secret.clientOsVersion,
            'X-Device-ID': secret.deviceId,
        },
        tags: {endpoint: endpointTag},
    };
}