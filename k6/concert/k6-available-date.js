import http from 'k6/http';
import { check, sleep } from 'k6';
import { options, BASE_URL } from '../common/test.js';

export { options };

export default function () {
    const concertId = Math.floor(Math.random() * 5) + 1;
    const concertsResponse = http.get(`${BASE_URL}/api/concert/dates/${concertId}`, {
        headers: { 'token': `token` },
    });

    check(concertsResponse, {
        'concerts status was 200': (r) => r.status === 200,
    });

    sleep(1);
}