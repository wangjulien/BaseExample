import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IHost } from 'app/shared/model/host.model';
import { getEntities as getHosts } from 'app/entities/host/host.reducer';
import { getEntity, updateEntity, createEntity, reset } from './service.reducer';
import { IService } from 'app/shared/model/service.model';
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IServiceUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IServiceUpdateState {
  isNew: boolean;
  hostId: string;
}

export class ServiceUpdate extends React.Component<IServiceUpdateProps, IServiceUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      hostId: '0',
      isNew: !this.props.match.params || !this.props.match.params.id
    };
  }

  componentWillUpdate(nextProps, nextState) {
    if (nextProps.updateSuccess !== this.props.updateSuccess && nextProps.updateSuccess) {
      this.handleClose();
    }
  }

  componentDidMount() {
    if (this.state.isNew) {
      this.props.reset();
    } else {
      this.props.getEntity(this.props.match.params.id);
    }

    this.props.getHosts();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { serviceEntity } = this.props;
      const entity = {
        ...serviceEntity,
        ...values
      };

      if (this.state.isNew) {
        this.props.createEntity(entity);
      } else {
        this.props.updateEntity(entity);
      }
    }
  };

  handleClose = () => {
    this.props.history.push('/service');
  };

  render() {
    const { serviceEntity, hosts, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="baseExampleApp.service.home.createOrEditLabel">Create or edit a Service</h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : serviceEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="service-id">ID</Label>
                    <AvInput id="service-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="serviceNameLabel" for="service-serviceName">
                    Service Name
                  </Label>
                  <AvField
                    id="service-serviceName"
                    type="text"
                    name="serviceName"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="statusLabel" for="service-status">
                    Status
                  </Label>
                  <AvInput
                    id="service-status"
                    type="select"
                    className="form-control"
                    name="status"
                    value={(!isNew && serviceEntity.status) || 'UP'}
                  >
                    <option value="UP">UP</option>
                    <option value="DOWN">DOWN</option>
                    <option value="UNKNOWN">UNKNOWN</option>
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="service-host">Host</Label>
                  <AvInput id="service-host" type="select" className="form-control" name="hostId">
                    <option value="" key="0" />
                    {hosts
                      ? hosts.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/service" replace color="info">
                  <FontAwesomeIcon icon="arrow-left" />
                  &nbsp;
                  <span className="d-none d-md-inline">Back</span>
                </Button>
                &nbsp;
                <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                  <FontAwesomeIcon icon="save" />
                  &nbsp; Save
                </Button>
              </AvForm>
            )}
          </Col>
        </Row>
      </div>
    );
  }
}

const mapStateToProps = (storeState: IRootState) => ({
  hosts: storeState.host.entities,
  serviceEntity: storeState.service.entity,
  loading: storeState.service.loading,
  updating: storeState.service.updating,
  updateSuccess: storeState.service.updateSuccess
});

const mapDispatchToProps = {
  getHosts,
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ServiceUpdate);
