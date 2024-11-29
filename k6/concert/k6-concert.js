import http from 'k6/http';
import { sleep, check } from 'k6';
import { options, BASE_URL } from '../common/test.js';

export { options }; // 사전에 생성된 토큰을 입력

export default function () {
    const concertsResponse = http.get(`${BASE_URL}/concert/list`);

    check(concertsResponse, {
        'concerts status was 200': (r) => r.status === 200
    });

    sleep(1);
}