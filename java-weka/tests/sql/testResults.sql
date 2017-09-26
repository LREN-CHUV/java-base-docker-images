BEGIN;

-- Plan the tests
SELECT plan( 5 );

SELECT is(node::VARCHAR, 'job_test', 'Job result node is incorrect')
  FROM job_result where job_id = '1';

SELECT is(function::VARCHAR, 'java-weka-rpm', 'Job result function is incorrect')
  FROM job_result where job_id = '1';

SELECT is(shape::VARCHAR, 'pfa_json', 'Job result shape is incorrect')
  FROM job_result where job_id = '1';

SELECT isnt(data::TEXT, NULL, 'Job result should contain data')
  FROM job_result where job_id = '1';

SELECT is(error::VARCHAR, NULL, 'Job result should not have error')
  FROM job_result where job_id = '1';

-- Clean up
SELECT * FROM finish();
ROLLBACK;
