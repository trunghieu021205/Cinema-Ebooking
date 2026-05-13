export interface MembershipTierResponse {
  id: number
  name: string
  minSpendingRequired: number   // BigDecimal → number
  discountPercent: number
  benefitsDescription: string
  tierLevel: number
}

export interface CreateMembershipTierRequest {
  name: string
  minSpendingRequired: number
  discountPercent: number
  benefitsDescription: string
  tierLevel: number
}

export type UpdateMembershipTierRequest = CreateMembershipTierRequest