import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
    scenarios: {
        fetch_data: {
            executor: 'ramping-vus',
            startVUs: 0,
            stages: [
                {duration: '15s', target: 5},
                {duration: '30s', target: 5},
                {duration: '15s', target: 0},
            ],
            gracefulRampDown: '15s',
            exec: 'fetchData',
        },
        populate_and_update_data: {
            executor: 'ramping-vus',
            startVUs: 0,
            stages: [
                {duration: '15s', target: 5},
                {duration: '30s', target: 5},
                {duration: '15s', target: 0},
            ],
            gracefulRampDown: '15s',
            exec: 'populateAndUpdateData',
        },
    },
    thresholds: {
        'http_req_duration{scenario:fetch_data}': ['p(95)<500'],
        'http_req_duration{scenario:populate_and_update_data}': ['p(95)<500'],
        'http_req_failed{scenario:fetch_data}': ['rate<0.01'],
        'http_req_failed{scenario:populate_and_update_data}': ['rate<0.01'],
        'checks{scenario:fetch_data}': ['rate>0.95'],
        'checks{scenario:populate_and_update_data}': ['rate>0.95'],
    },
};

const SERVICE_URL = 'http://app:8080/products/v1';

export function fetchData() {

    const getAllProducts = http.get(`${SERVICE_URL}`, {tags: {name: 'FetchAllProducts'}});

    check(getAllProducts,
        {'is status 200': (r) => r.status === 200},
        {name: 'FetchAllProducts'}
    );
}

export function populateAndUpdateData() {
    const newProductDto = {
        summary: 'test_summary',
        description: 'test_description',
        price: 100.0,
        duration: 30,
        discountId: 1,
        active: true
    };

    const createProduct = http.post(`${SERVICE_URL}`, JSON.stringify(newProductDto), {
        headers: {'Content-Type': 'application/json',},
        tags: {name: 'CreateProduct'}
    });

    check(createProduct,
        {'is status 201': (r) => r.status === 201},
        {name: 'CreateProduct'}
    );

    const createdProductId = JSON.parse(createProduct.body).id;

    const updateProductDto = {
        summary: 'updated_test_summary',
        description: 'updated_test_description',
        price: 150.0,
        duration: 45,
        discountId: 2,
        active: false
    };

    const updateProduct = http.put(`${SERVICE_URL}/${createdProductId}`, JSON.stringify(updateProductDto), {
        headers: {'Content-Type': 'application/json',},
        tags: {name: 'UpdateProduct'}
    });

    check(updateProduct,
        {'is status 200': (r) => r.status === 200},
        {name: 'UpdateProduct'}
    );
}
