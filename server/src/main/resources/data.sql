INSERT INTO users (
  username,
  password,
  enabled
) VALUES (
  'dave',
  '$2a$10$L19dxrqfOawStLsRwkt6t.AEBO0p3321s/8Sg9Ulh1OzkVxOWf/1.', -- secret
  TRUE
);
INSERT INTO authorities (username, authority) VALUES ('dave', 'ROLE_USER');

INSERT INTO oauth_client_details (
  client_id,
  client_secret,
  scope,
  authorized_grant_types,
  web_server_redirect_uri,
  authorities,
  access_token_validity
) VALUES (
  'my-trusted-client',
  '$2a$10$lX0B5Nu3qhFJi4wDyabMne2OwQoEa7eRVNY7w/4VDdLBCXgYS4/wu', -- my-trusted-client-pass
  'read,write,trust',
  'password,authorization_code,refresh_token,implicit',
  'http://localhost:9999/client',
  'ROLE_CLIENT,ROLE_TRUSTED_CLIENT',
  60
);
INSERT INTO oauth_client_details (
  client_id,
  client_secret,
  scope,
  authorized_grant_types,
  authorities
) VALUES (
  'my-resource',
  '$2a$10$An5EsiCaW60oUev8sNWQhO2TCjjLGnAaMQk.RTIM7rNCKAKeCvYqO', -- my-resource-pass
  'read,write,trust',
  'client',
  'ROLE_RESOURCE'
);
