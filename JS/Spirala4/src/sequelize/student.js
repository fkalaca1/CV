module.exports = (sequelize, type) => {
    return sequelize.define('Student', {
        id: {
          type: type.INTEGER,
          primaryKey: true,
          autoIncrement: true
        },
        imePrezime: type.STRING,
        index: {
            type: type.STRING,
        }
    }, {
        indexes: [
            {
                unique: true,
                fields: ['index']
            }
        ]
    })
}