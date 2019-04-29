module.exports = (sequelize, type) => {
    return sequelize.define('Godina', {
        id: {
          type: type.INTEGER,
          primaryKey: true,
          autoIncrement: true
        },
        nazivGod: type.STRING,
        nazivRepSpi: type.STRING,
        nazivRepVje: type.STRING,
    }, {
        indexes: [
            {
                unique: true,
                fields: ['nazivGod']
            }
        ]
    })
}