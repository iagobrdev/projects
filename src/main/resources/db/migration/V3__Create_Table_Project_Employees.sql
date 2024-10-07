CREATE TABLE IF NOT EXISTS project_employees (
  id BIGSERIAL PRIMARY KEY,
  project BIGINT NOT NULL,
  employee BIGINT NOT NULL,
  FOREIGN KEY (project) REFERENCES projects(id),
  FOREIGN KEY (employee) REFERENCES employees(id)
);