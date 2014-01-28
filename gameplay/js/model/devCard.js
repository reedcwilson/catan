
catan = catan || {};
catan.models = catan.models || {};

catan.models.DevCard = (function devCardNamespace() {

  /**
    The DevCard class is an interface for all of the development cards
    <pre>
      Invariants:
        INVARIANT: has a cardType

      Constructor Specification:
        PRE: valid cardType value
        POST: getType() === cardType
    </pre>

    @class DevCard
    @constructor

    @param {cardType} the type of development card
    */
  function DevCard(type) { };

});
